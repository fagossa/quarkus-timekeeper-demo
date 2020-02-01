package com.lunatech.controllers;

import com.lunatech.forms.FormFieldWithErrors;
import com.lunatech.forms.Validation;
import com.lunatech.models.TimeEntry;
import com.lunatech.models.TimeEntryDTO;
import com.lunatech.services.TimeEntryService;
import io.quarkus.qute.Engine;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.vavr.control.Either;
import org.jboss.resteasy.annotations.Form;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

import org.jboss.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;


@Path("/times")
@Produces(MediaType.TEXT_HTML)
@ApplicationScoped
public class TimeEntryController {

    private static final Logger logger = Logger.getLogger(TimeEntryController.class);

    @Inject
    TimeEntryService timeEntryService;

    @Inject
    Template timeEntries;


    @Inject
    Template timeEntry;

    @Inject
    Template newTimeEntry;

    @Inject
    Engine engine;

    Validation<TimeEntry> validation = new Validation<>();

    @GET
    public TemplateInstance list() {
        List<TimeEntry> entries = TimeEntry.listAll();
        return timeEntries.data("timeEntries", entries);
    }

    @GET
    @Path("/{id}")
    public TemplateInstance get(@PathParam("id") Long id) {
        TimeEntry entry = timeEntryService.findTimeEntryById(id);
        if (entry == null) {
            throw new WebApplicationException("This time entry does not exist", 404);
        }
        return timeEntry.data("timeEntry", entry);
    }

    @GET
    @Path("/new")
    public TemplateInstance prepareNew() {
        return newTimeEntry.data("form", new com.lunatech.forms.Form("/times/new"));
    }

    @POST
    @Path("/new")
    @Consumes(APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response save(@Form TimeEntryDTO timeEntryDTO) {
        Either<FormFieldWithErrors, TimeEntry> validTimeEntryOrError = validation.validate(timeEntryDTO);

        if (validTimeEntryOrError.isLeft()) {
            // A best practice is not to throw a WebApplicationException here, but to return a 400 Bad Request
            // with the Form and a list of errors.
            // In play2 framework we have a Form / FormFactory
            FormFieldWithErrors formErrors = validTimeEntryOrError.getLeft();
            logger.warn("Unable to persist a TimeEntry. Reason : " + formErrors.getErrorMessage());
            Object htmlContent = newTimeEntry.data("form", new com.lunatech.forms.Form("/times/new", formErrors));
            return Response.status(400, formErrors.getErrorMessage()).entity(htmlContent).build();
        } else {
            TimeEntry newTimeEntry = validTimeEntryOrError.get();
            timeEntryService.persist(newTimeEntry);
            return Response.seeOther(URI.create("/times")).build();
        }
    }

}
