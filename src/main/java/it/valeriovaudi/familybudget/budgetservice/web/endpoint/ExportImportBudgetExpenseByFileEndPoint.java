package it.valeriovaudi.familybudget.budgetservice.web.endpoint;

import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Month;
import it.valeriovaudi.familybudget.budgetservice.domain.model.time.Year;
import it.valeriovaudi.familybudget.budgetservice.domain.usecase.ExportSpentBudget;
import it.valeriovaudi.familybudget.budgetservice.domain.usecase.LoadSpentBudget;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/budget-expense")
public class ExportImportBudgetExpenseByFileEndPoint {

    private final ExportSpentBudget csvSpentBudgetExporter;
    private final ExportSpentBudget pdfSpentBudgetExporter;
    private final ExportSpentBudget xslxDataExporter;
    private final LoadSpentBudget loadSpentBudget;

    public ExportImportBudgetExpenseByFileEndPoint(ExportSpentBudget csvSpentBudgetExporter,
                                                   ExportSpentBudget pdfSpentBudgetExporter,
                                                   ExportSpentBudget xslxDataExporter,
                                                   LoadSpentBudget loadSpentBudget) {
        this.csvSpentBudgetExporter = csvSpentBudgetExporter;
        this.pdfSpentBudgetExporter = pdfSpentBudgetExporter;
        this.xslxDataExporter = xslxDataExporter;
        this.loadSpentBudget = loadSpentBudget;
    }

    @GetMapping(produces = "application/csv")
    public ResponseEntity<byte[]> getAsCsv(@RequestParam("month") Integer month,
                                           @RequestParam(value = "year", required = false) String year) throws IOException {
        log.info("year" + year);
        return getFile(month, getYear(year), csvSpentBudgetExporter, "csv");
    }

    @GetMapping(produces = "application/xlsx")
    public ResponseEntity<byte[]> getAsXslx(@RequestParam("month") Integer month,
                                            @RequestParam(value = "year", required = false) String year) throws IOException {
        log.info("year" + year);
        return getFile(month, getYear(year), xslxDataExporter, "xlsx");
    }

    @GetMapping(produces = "application/pdf")
    public ResponseEntity<byte[]> getAsPdf(@RequestParam("month") Integer month,
                                           @RequestParam(value = "year", required = false) String year) throws IOException {
        log.info("year" + year);
        return getFile(month, getYear(year), pdfSpentBudgetExporter, "pdf");
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity loadByCsv(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        loadSpentBudget.importFrom(multipartFile.getInputStream());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private Year getYear(String year) {
        return Optional.ofNullable(year).map(Integer::parseInt).map(Year::of).orElse(Year.now());
    }

    private ResponseEntity<byte[]> getFile(Integer month,
                                           Year year, ExportSpentBudget spentBudgetExporter,
                                           String applicationMediaType) throws IOException {
        Month monthAux = Month.of(month);
        byte[] content;

        try (InputStream inputStream = spentBudgetExporter.exportFrom(year, monthAux)) {
            content = inputStream.readAllBytes();
        }

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", applicationMediaType));
        header.set("Content-Disposition", String.format("inline; filename=%s-%s.%s", monthAux.localizedMonthName(Locale.ENGLISH), year.getYearValue(), applicationMediaType));
        header.setContentLength(content.length);

        return ResponseEntity.ok().headers(header).body(content);
    }

}