'''
    Read a list of urls, load the pages and save as a single pdf.
    File urls.txt must be available alongside this script.
    After running, resulting pdf is available as 'generated.pdf'
'''

from weasyprint import HTML
# cli: pip3 install weasyprint
from PyPDF2 import PdfMerger
# cli: pip3 install PyPDF2
from io import BytesIO

def run():

    merger = PdfMerger(strict=False)

    print("running...")

    urls = open("urls.txt")

    for idx, url in enumerate(urls.read().splitlines()):
        pdf = pdfify(url)
        merger.append(pdf)
        print(f'processed {url}')

    urls.close()

    output = open("generated.pdf", "wb")
    merger.write(output)

    print("ready!")


def pdfify(url):

    pdfout = HTML(url).write_pdf() # bytes
    obj = BytesIO()
    obj.write(pdfout) # File object
    return obj

if __name__ == "__main__":
    run()


