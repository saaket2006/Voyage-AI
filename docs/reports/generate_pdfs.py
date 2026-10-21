#!/usr/bin/env python3
"""
Voyage-AI PDF Documentation Suite Generator.
Converts Markdown reports to publication-grade, implementation-verified PDFs using
Microsoft Edge Chromium headless print-to-pdf engine with PyMuPDF validation.
"""

import os
import sys
import subprocess
import re
import markdown
import fitz  # PyMuPDF

EDGE_EXE = r"C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe"
REPORTS_DIR = os.path.dirname(os.path.abspath(__file__))

CSS_STYLES = """
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&family=Outfit:wght@500;600;700;800&family=JetBrains+Mono:wght@400;500&display=swap');

@page {
    size: A4;
    margin: 18mm 16mm 20mm 16mm;
    @bottom-center {
        content: "Voyage-AI Implementation-Verified Documentation Suite";
        font-family: 'Inter', sans-serif;
        font-size: 8pt;
        color: #94a3b8;
    }
}

*, *::before, *::after {
    box-sizing: border-box;
}

body {
    font-family: 'Inter', -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
    color: #1e293b;
    background-color: #ffffff;
    line-height: 1.6;
    font-size: 9.5pt;
    margin: 0;
    padding: 0;
}

/* Header & Typography */
h1, h2, h3, h4, h5, h6 {
    font-family: 'Outfit', sans-serif;
    color: #0f172a;
    font-weight: 700;
    line-height: 1.25;
    margin-top: 1.4em;
    margin-bottom: 0.5em;
    page-break-after: avoid;
}

h1 {
    font-size: 20pt;
    color: #1e3a8a;
    border-bottom: 2.5px solid #ea580c;
    padding-bottom: 8px;
    margin-top: 0;
    letter-spacing: -0.02em;
}

h2 {
    font-size: 13.5pt;
    color: #1e3a8a;
    border-bottom: 1px solid #e2e8f0;
    padding-bottom: 4px;
    margin-top: 1.6em;
    letter-spacing: -0.01em;
}

h3 {
    font-size: 11pt;
    color: #334155;
    margin-top: 1.2em;
}

h4 {
    font-size: 10pt;
    color: #475569;
}

p, ul, ol {
    margin-top: 0;
    margin-bottom: 0.8em;
}

li {
    margin-bottom: 0.3em;
}

/* Document Header Meta Plate */
.doc-header-meta {
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-left: 4px solid #1e3a8a;
    border-radius: 6px;
    padding: 10px 14px;
    margin-bottom: 18px;
    font-size: 8.5pt;
    color: #475569;
}

.doc-header-meta p {
    margin: 0;
    line-height: 1.5;
}

.doc-header-meta strong {
    color: #0f172a;
}

/* Tables */
table {
    width: 100%;
    border-collapse: collapse;
    margin: 14px 0;
    font-size: 8pt;
    page-break-inside: auto;
}

tr {
    page-break-inside: avoid;
    page-break-after: auto;
}

thead {
    display: table-header-group;
}

th {
    background-color: #1e3a8a;
    color: #ffffff;
    font-family: 'Outfit', sans-serif;
    font-weight: 600;
    text-align: left;
    padding: 7px 9px;
    border: 1px solid #1e3a8a;
    letter-spacing: 0.01em;
}

td {
    padding: 6px 9px;
    border: 1px solid #cbd5e1;
    color: #334155;
    vertical-align: top;
}

tbody tr:nth-child(even) {
    background-color: #f8fafc;
}

tbody tr:hover {
    background-color: #f1f5f9;
}

/* Code Blocks & Architecture Diagrams */
pre {
    background-color: #0f172a;
    color: #f8fafc;
    font-family: 'JetBrains Mono', Consolas, "Courier New", monospace;
    font-size: 7.2pt;
    line-height: 1.45;
    padding: 10px 14px;
    border-radius: 6px;
    overflow-x: auto;
    border: 1px solid #1e293b;
    margin: 12px 0;
    page-break-inside: avoid;
    white-space: pre;
}

code {
    font-family: 'JetBrains Mono', Consolas, "Courier New", monospace;
    font-size: 8pt;
    background-color: #f1f5f9;
    color: #0f172a;
    padding: 1.5px 4px;
    border-radius: 4px;
    border: 1px solid #e2e8f0;
}

pre code {
    background-color: transparent;
    color: inherit;
    padding: 0;
    border: none;
    font-size: inherit;
}

/* Callout Boxes */
blockquote {
    background: #f8fafc;
    border-left: 3.5px solid #ea580c;
    margin: 12px 0;
    padding: 8px 12px;
    font-size: 9pt;
    color: #475569;
    border-radius: 0 6px 6px 0;
}

blockquote p {
    margin: 0;
}

/* Links */
a {
    color: #1e3a8a;
    text-decoration: none;
    font-weight: 500;
}

a:hover {
    text-decoration: underline;
    color: #ea580c;
}

/* Badges & Tags */
.badge {
    display: inline-block;
    padding: 2px 7px;
    font-size: 7pt;
    font-weight: 600;
    border-radius: 9999px;
    text-transform: uppercase;
    letter-spacing: 0.05em;
}

.badge-verified {
    background: #ecfdf5;
    color: #065f46;
    border: 1px solid #a7f3d0;
}

.badge-pending {
    background: #fffbeb;
    color: #92400e;
    border: 1px solid #fde68a;
}

/* Footer & Page Breaks */
.page-break {
    page-break-before: always;
}

.footer-print {
    margin-top: 24px;
    padding-top: 10px;
    border-top: 1px solid #e2e8f0;
    font-size: 7.5pt;
    color: #64748b;
    display: flex;
    justify-content: space-between;
    align-items: center;
}
"""

REPORTS = [
    {
        "id": "architecture",
        "title": "Voyage-AI — System Architecture & Technical Design",
        "source": "architecture.md",
        "output": "architecture.pdf"
    },
    {
        "id": "methodology",
        "title": "Voyage-AI — Engineering Methodology & Operational Workflows",
        "source": "methodology.md",
        "output": "methodology.pdf"
    },
    {
        "id": "project-report",
        "title": "Voyage-AI — Comprehensive Project Report",
        "source": "project-report.md",
        "output": "project-report.pdf"
    },
    {
        "id": "implementation-status",
        "title": "Voyage-AI — Implementation Status & Feature Inventory",
        "source": "implementation-status.md",
        "output": "implementation-status.pdf"
    },
    {
        "id": "testing-and-quality",
        "title": "Voyage-AI — Testing Strategy & Software Quality Assessment",
        "source": "testing-and-quality.md",
        "output": "testing-and-quality.pdf"
    },
    {
        "id": "risks-and-roadmap",
        "title": "Voyage-AI — Technical Risks & Prioritized Engineering Roadmap",
        "source": "risks-and-roadmap.md",
        "output": "risks-and-roadmap.pdf"
    },
    {
        "id": "README",
        "title": "Voyage-AI — Documentation Suite Index & Navigation Guide",
        "source": "README.md",
        "output": "README.pdf"
    }
]

def render_markdown_to_html(md_content, title):
    """Converts Markdown text to full HTML5 with custom typography and CSS."""
    # Convert markdown with extensions
    extensions = ['tables', 'fenced_code', 'toc', 'attr_list', 'nl2br']
    html_body = markdown.markdown(md_content, extensions=extensions)

    # Wrap in complete HTML document
    html_full = f"""<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>{title}</title>
    <style>
{CSS_STYLES}
    </style>
</head>
<body>
    <div class="content-wrapper">
{html_body}
    </div>
    <div class="footer-print">
        <span>Voyage-AI (Phase 1 Baseline) · Implementation-Verified Documentation</span>
        <span>Generated: October 21, 2026 · Confidential & Proprietary</span>
    </div>
</body>
</html>"""
    return html_full

def generate_pdf_via_edge(html_path, pdf_path):
    """Invokes Microsoft Edge Chromium in headless mode to render HTML to PDF."""
    file_uri = "file:///" + html_path.replace("\\", "/")
    cmd = [
        EDGE_EXE,
        "--headless",
        "--disable-gpu",
        "--run-all-compositor-stages-before-draw",
        "--no-pdf-header-footer",
        f"--print-to-pdf={pdf_path}",
        file_uri
    ]
    result = subprocess.run(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
    return result.returncode == 0 and os.path.exists(pdf_path)

def validate_pdf(pdf_path):
    """Validates the PDF using PyMuPDF (fitz). Returns dict with page count, size, and text extractability."""
    if not os.path.exists(pdf_path):
        return {"valid": False, "error": "File does not exist"}
    
    size_bytes = os.path.getsize(pdf_path)
    if size_bytes < 1000:
        return {"valid": False, "error": f"File too small ({size_bytes} bytes)"}
    
    try:
        doc = fitz.open(pdf_path)
        page_count = len(doc)
        total_text_length = 0
        pages_with_text = 0

        for i, page in enumerate(doc):
            text = page.get_text()
            if text and len(text.strip()) > 20:
                pages_with_text += 1
                total_text_length += len(text)
        
        doc.close()

        valid = page_count > 0 and pages_with_text == page_count
        return {
            "valid": valid,
            "page_count": page_count,
            "size_bytes": size_bytes,
            "pages_with_text": pages_with_text,
            "total_text_length": total_text_length
        }
    except Exception as ex:
        return {"valid": False, "error": str(ex)}

def main():
    print("=" * 70)
    print("  Voyage-AI — Automated PDF Documentation Suite Generation")
    print("=" * 70)
    print(f"Reports Directory: {REPORTS_DIR}")
    print(f"Edge Binary:       {EDGE_EXE}")
    print()

    if not os.path.exists(EDGE_EXE):
        print(f"ERROR: Microsoft Edge not found at {EDGE_EXE}")
        sys.exit(1)

    validation_results = []
    all_success = True

    for report in REPORTS:
        src_path = os.path.join(REPORTS_DIR, report["source"])
        pdf_path = os.path.join(REPORTS_DIR, report["output"])
        temp_html_path = os.path.join(REPORTS_DIR, f"{report['id']}.tmp.html")

        print(f"[*] Processing: {report['source']} -> {report['output']}...")

        if not os.path.exists(src_path):
            print(f"  [ERROR] Source markdown file missing: {src_path}")
            all_success = False
            continue

        with open(src_path, "r", encoding="utf-8") as f:
            md_content = f.read()

        html_content = render_markdown_to_html(md_content, report["title"])
        with open(temp_html_path, "w", encoding="utf-8") as f:
            f.write(html_content)

        # Generate PDF
        success = generate_pdf_via_edge(temp_html_path, pdf_path)

        # Cleanup temporary HTML
        if os.path.exists(temp_html_path):
            os.remove(temp_html_path)

        if not success or not os.path.exists(pdf_path):
            print(f"  [FAIL] Failed to generate PDF: {pdf_path}")
            all_success = False
            continue

        # Validate with PyMuPDF
        audit = validate_pdf(pdf_path)
        audit["report_id"] = report["id"]
        audit["filename"] = report["output"]
        validation_results.append(audit)

        if audit.get("valid"):
            print(f"  [PASS] {report['output']} | Pages: {audit['page_count']} | Size: {audit['size_bytes']:,} bytes | Text: {audit['total_text_length']:,} chars")
        else:
            print(f"  [FAIL] {report['output']} failed validation: {audit.get('error')}")
            all_success = False

    print()
    print("=" * 70)
    print("  FINAL DOCUMENTATION SUITE VALIDATION SUMMARY")
    print("=" * 70)
    print(f"{'Report File':<28} | {'Pages':<6} | {'Size (KB)':<10} | {'Extractable Text':<18} | {'Status'}")
    print("-" * 75)

    for item in validation_results:
        size_kb = f"{item['size_bytes'] / 1024:.1f} KB"
        chars = f"{item['total_text_length']:,} chars"
        status = "VERIFIED PASS" if item["valid"] else "FAILED"
        print(f"{item['filename']:<28} | {item['page_count']:<6} | {size_kb:<10} | {chars:<18} | {status}")

    print("=" * 75)

    if all_success and len(validation_results) == 7:
        print("\nAll 7 required reports were successfully compiled, validated, and saved to docs/reports/.")
        sys.exit(0)
    else:
        print("\nOne or more reports failed generation or validation.")
        sys.exit(1)

if __name__ == "__main__":
    main()
