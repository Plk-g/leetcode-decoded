#!/usr/bin/env python3
"""
Scan pattern folders for solution .java files, parse LeetCode metadata from the
block Javadoc, refresh tracker.json, and regenerate README auto-sections.
"""
from __future__ import annotations

import json
import os
import re
import sys
from datetime import datetime, timezone
from pathlib import Path
from urllib.parse import quote

ROOT = Path(__file__).resolve().parent
README_PATH = ROOT / "README.md"
TRACKER_PATH = ROOT / "tracker.json"
TOTAL_TARGET = 150

# (folder_name, display pattern name, NeetCode-style target count for X / Y row; sums to 150)
CATEGORY_ROWS: list[tuple[str, str, int]] = [
    ("arrays-and-hashing", "Arrays & Hashing", 14),
    ("two-pointers", "Two Pointers", 11),
    ("sliding-window", "Sliding Window", 11),
    ("stack", "Stack", 11),
    ("binary-search", "Binary Search", 11),
    ("linked-list", "Linked List", 13),
    ("trees", "Trees", 15),
    ("heap-priority-queue", "Heap / Priority Queue", 11),
    ("backtracking", "Backtracking", 11),
    ("graphs", "Graphs", 13),
    ("dynamic-programming", "Dynamic Programming", 18),
    ("math-and-geometry", "Math & Geometry", 11),
]

EXCLUDE_JAVA = {"TEMPLATE.java"}

NUM_TITLE_RE = re.compile(r"^(\d+)\.\s*(.+?)\s*$")
DIFF_RE = re.compile(r"^Difficulty:\s*(\S+)", re.I)
PATTERN_RE = re.compile(r"^Pattern:\s*(.+?)\s*$", re.I)
LINK_RE = re.compile(r"^Link:\s*(\S+)", re.I)


def extract_javadoc(source: str) -> str | None:
    start = source.find("/**")
    if start == -1:
        return None
    end = source.find("*/", start)
    if end == -1:
        return None
    return source[start + 3 : end]


def javadoc_lines(doc: str) -> list[str]:
    lines: list[str] = []
    for raw in doc.splitlines():
        line = raw.strip()
        if line.startswith("*"):
            line = line[1:].strip()
        lines.append(line)
    return lines


def parse_solution_file(path: Path) -> dict | None:
    try:
        text = path.read_text(encoding="utf-8")
    except OSError as e:
        print(f"warn: could not read {path}: {e}", file=sys.stderr)
        return None
    doc = extract_javadoc(text)
    if not doc:
        return None
    lines = javadoc_lines(doc)
    number: int | None = None
    title: str | None = None
    difficulty: str | None = None
    pattern: str | None = None
    link: str | None = None
    for line in lines:
        if not line:
            continue
        if line.startswith("PROBLEM:"):
            break
        m = NUM_TITLE_RE.match(line)
        if m:
            number = int(m.group(1))
            title = m.group(2).strip()
            continue
        m = DIFF_RE.match(line)
        if m:
            difficulty = m.group(1).strip()
            continue
        m = PATTERN_RE.match(line)
        if m:
            pattern = m.group(1).strip()
            continue
        m = LINK_RE.match(line)
        if m:
            link = m.group(1).strip()
            continue
    if number is None or not title:
        print(f"warn: missing number/title in {path}", file=sys.stderr)
        return None
    rel = path.relative_to(ROOT).as_posix()
    return {
        "number": number,
        "title": title,
        "difficulty": difficulty or "?",
        "pattern": pattern or "?",
        "link": link or "",
        "path": rel,
    }


def discover_problems() -> list[dict]:
    found: list[dict] = []
    for folder, _, _ in CATEGORY_ROWS:
        d = ROOT / folder
        if not d.is_dir():
            continue
        for p in sorted(d.glob("*.java")):
            if p.name in EXCLUDE_JAVA:
                continue
            meta = parse_solution_file(p)
            if meta:
                found.append(meta)
    found.sort(key=lambda x: (x["number"], x["title"]))
    return found


def counts_by_folder() -> dict[str, int]:
    counts: dict[str, int] = {}
    for folder, _, _ in CATEGORY_ROWS:
        d = ROOT / folder
        if not d.is_dir():
            counts[folder] = 0
            continue
        counts[folder] = sum(
            1 for p in d.glob("*.java") if p.name not in EXCLUDE_JAVA
        )
    return counts


def replace_readme_section(readme: str, start_tag: str, end_tag: str, body: str) -> str:
    pattern = re.compile(
        re.escape(start_tag) + r".*?" + re.escape(end_tag),
        flags=re.DOTALL,
    )
    if not pattern.search(readme):
        raise ValueError(f"README missing section markers: {start_tag} … {end_tag}")
    repl = f"{start_tag}\n{body.rstrip()}\n{end_tag}"
    return pattern.sub(repl, readme, count=1)


def build_badge_markdown() -> str:
    gh = os.environ.get("GITHUB_REPOSITORY")
    if gh:
        owner, repo = gh.split("/", 1)
        branch = os.environ.get("GITHUB_REF_NAME", "main")
        raw = (
            f"https://raw.githubusercontent.com/{owner}/{repo}/refs/heads/{branch}/tracker.json"
        )
        enc = quote(raw, safe="")
        suf = quote(f"/{TOTAL_TARGET}", safe="")
        return (
            f"![Solved](https://img.shields.io/badge/dynamic/json?url={enc}&query=%24.solved"
            f"&label=solved&suffix={suf}&color=brightgreen) "
            f"![Language](https://img.shields.io/badge/language-Java-orange) "
            f"![Roadmap](https://img.shields.io/badge/roadmap-NeetCode%20150-blue)"
        )
    placeholder = quote(
        "https://raw.githubusercontent.com/YOUR_GITHUB_USERNAME/Leetcode/refs/heads/main/tracker.json",
        safe="",
    )
    suf = quote(f"/{TOTAL_TARGET}", safe="")
    return (
        f"![Solved](https://img.shields.io/badge/dynamic/json?url={placeholder}&query=%24.solved"
        f"&label=solved&suffix={suf}&color=brightgreen) "
        f"![Language](https://img.shields.io/badge/language-Java-orange) "
        f"![Roadmap](https://img.shields.io/badge/roadmap-NeetCode%20150-blue)"
    )


def build_tracker_table(problems: list[dict]) -> str:
    lines = [
        "| # | Problem | Difficulty | Pattern | Solution |",
        "|---|---------|------------|---------|----------|",
    ]
    for p in problems:
        num = p["number"]
        title = p["title"]
        lc = p["link"] or "#"
        diff = p["difficulty"]
        pat = p["pattern"]
        path = p["path"]
        lines.append(
            f"| {num} | [{title}]({lc}) | {diff} | {pat} | [code]({path}) |"
        )
    if len(problems) == 0:
        lines.append("| — | *Add solutions; run `python update_tracker.py`* | — | — | — |")
    return "\n".join(lines)


def build_patterns_table(counts: dict[str, int]) -> str:
    lines = [
        "| Pattern | Progress (solved / target) |",
        "|---------|---------------------------|",
    ]
    for folder, display, target in CATEGORY_ROWS:
        x = counts.get(folder, 0)
        lines.append(f"| {display} | {x} / {target} |")
    return "\n".join(lines)


def main() -> int:
    problems = discover_problems()
    solved = len(problems)
    now = datetime.now(timezone.utc).strftime("%Y-%m-%dT%H:%M:%SZ")
    tracker = {
        "solved": solved,
        "total": TOTAL_TARGET,
        "last_updated": now,
        "problems": problems,
    }
    TRACKER_PATH.write_text(json.dumps(tracker, indent=2) + "\n", encoding="utf-8")

    if README_PATH.exists():
        readme = README_PATH.read_text(encoding="utf-8")
        try:
            readme = replace_readme_section(
                readme,
                "<!-- AUTO:SOLVED_BADGES -->",
                "<!-- END_AUTO:SOLVED_BADGES -->",
                build_badge_markdown(),
            )
            readme = replace_readme_section(
                readme,
                "<!-- AUTO:TRACKER_TABLE -->",
                "<!-- END_AUTO:TRACKER_TABLE -->",
                build_tracker_table(problems),
            )
            readme = replace_readme_section(
                readme,
                "<!-- AUTO:PATTERNS_OVERVIEW -->",
                "<!-- END_AUTO:PATTERNS_OVERVIEW -->",
                build_patterns_table(counts_by_folder()),
            )
            README_PATH.write_text(readme, encoding="utf-8")
        except ValueError as e:
            print(f"warn: {e}", file=sys.stderr)

    human_date = datetime.now(timezone.utc).strftime("%Y-%m-%d %H:%M UTC")
    print(f"{solved}/{TOTAL_TARGET} solved. Last updated: {human_date}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
