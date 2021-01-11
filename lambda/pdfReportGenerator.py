from pathlib import Path

import PyPDF2


def lambda_handler(event, context):
    print(event)

    dailyBudgetExpenseRepresentationList = event["dailyBudgetExpenseRepresentationList"]
    print(dailyBudgetExpenseRepresentationList)
    newFileName = str(Path.home()) + "/test.pdf"
    pdfWriter = PyPDF2.PdfFileWriter()

    # new pdf file object
    newFile = open(newFileName, 'wb')

    pdfWriter.addBlankPage(100, 100)
    page = pdfWriter.getPage(0)
    print(page)
    # writing rotated pages to new file
    pdfWriter.write(newFile)

    # closing the new pdf file object
    newFile.close()

    return {
        'statusCode': 200
    }


if __name__ == '__main__':
    lambda_handler({"dailyBudgetExpenseRepresentationList": {}}, {})
