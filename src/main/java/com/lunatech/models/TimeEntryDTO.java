package com.lunatech.models;

import com.lunatech.forms.FormFieldWithErrors;
import com.lunatech.forms.Validatable;
import io.vavr.control.Either;

import javax.ws.rs.FormParam;
import java.time.Duration;
import java.time.LocalDate;

public class TimeEntryDTO implements Validatable<TimeEntry> {

    @FormParam("description")
    public String description;

    @FormParam("author")
    public String author;

    @FormParam("duration")
    public String durationAsString;

    @Override
    public String toString() {
        return "TimeEntryDTO{" +
                "description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", durationAsString='" + durationAsString + '\'' +
                '}';
    }

    @Override
    public Either<FormFieldWithErrors, TimeEntry> valid() {

        FormFieldWithErrors errors = FormFieldWithErrors
                .prepareNew()
                .nonEmpty("description", description)
                .nonEmpty("author", author);

        if (errors.hasErrors()) {
            return Either.left(errors);
        } else {
            TimeEntry timeEntry = new TimeEntry();
            timeEntry.description = this.description;
            timeEntry.author = this.author;
            timeEntry.entryDate = LocalDate.now(); // UTC ?
            timeEntry.duration = Duration.parse(this.durationAsString);
            return Either.right(timeEntry);
        }

    }
}
