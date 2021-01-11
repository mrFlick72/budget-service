from pathlib import Path

from reportlab.lib import colors
from reportlab.lib.pagesizes import A4, inch
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus import SimpleDocTemplate, Paragraph
from reportlab.platypus.tables import Table, TableStyle
from reportlab.rl_settings import defaultPageSize

PAGE_HEIGHT = defaultPageSize[1];
PAGE_WIDTH = defaultPageSize[0]
styles = getSampleStyleSheet()

if __name__ == '__main__':
    Title = "Hello world"
    pageinfo = "platypus example"
    newFileName = str(Path.home()) + "/test.pdf"

    doc = SimpleDocTemplate(newFileName, pagesize=A4, rightMargin=30, leftMargin=30, topMargin=30, bottomMargin=18)

    Story = []
    style = styles["Normal"]
    p = Paragraph("hijwhv", style)
    Story.append(p)

    style = TableStyle([('ALIGN', (0, 0), (4, 4), 'LEFT'),
                        ('GRID',(0,1),(-1,-1),0.01*inch,(0,0,0,)),
                        ('TEXTCOLOR', (0, 0), (4, 4), colors.black),
                        ('VALIGN', (0, 0), (4, 4), 'MIDDLE'),
                        ('BOX', (0, 0), (4, 5), 0.25, colors.black),
                        ])
    data = [
        ["Data", "Amount", "Note", "Tag", "Total"],
        ["1", "2", "3", "4", "5"],
        ["1", "2", "3", "4", "5"],
        ["1", "2", "3", "4", "5"],
        ["1", "2", "3", "4", "5"],
        ["1", "2", "3", "4", "5"]
    ]
    table = Table(data)
    table.setStyle(style)

    Story.append(table)

    doc.build(Story)
