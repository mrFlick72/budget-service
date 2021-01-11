from pathlib import Path

from reportlab.lib.colors import blue
from reportlab.lib.pagesizes import A4
from reportlab.lib.units import inch
from reportlab.pdfgen.canvas import Canvas
from reportlab.platypus.tables import Table

cm = 2.54

if __name__ == '__main__':
    newFileName = str(Path.home()) + "/test.pdf"

    canvas = Canvas(newFileName, pagesize=A4)
    canvas.setFont("Times-Roman", 12)

    # Draw blue text one inch from the left and ten
    # inches from the bottom
    canvas.setFillColor(blue)
    canvas.drawString(1 * inch, 10 * inch, "Blue text")

    textobject = canvas.beginText(1 * inch, 9.2 * inch)
    textobject.textLine("ciao come va")
    canvas.drawText(textobject)

    data = [(1, 2), (3, 4)]
    table = Table(data, colWidths=270, rowHeights=79)

    # table.drawOn(canvas, 1 * inch, 10 * inch)
    # Save the PDF file
    canvas.save()
