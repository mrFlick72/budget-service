from pathlib import Path

from fpdf import FPDF, HTMLMixin


class WriteHtmlPDF(FPDF, HTMLMixin):
    pass


if __name__ == '__main__':
    newFileName = str(Path.home()) + "/test.pdf"
    document = WriteHtmlPDF()
    document.set_font('Times', '', 12)
    document.add_page()

    html = """
    <h2>Budget Expense List from {start_time} to {end_time}</h2>
    <table table_offset="0"> 
        <thead>
            <tr>
                <th width="100">Data</th>
                <th width="100">Amount</th>
                <th width="100">Note</th>
                <th width="100">Tag</th>
                <th width="100">Total</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td width="100">cell 1</td>
                <td width="100">cell 2</td>
                <td width="100">cell 2</td>
                <td width="100">cell 2</td>
                <td width="100">cell 2</td>
            </tr>
        </tbody>
    </table>
    """
    text = html.format(start_time="10/10/2002", end_time="10/10/2002")
    document.write_html(text)

    document.output(newFileName)
