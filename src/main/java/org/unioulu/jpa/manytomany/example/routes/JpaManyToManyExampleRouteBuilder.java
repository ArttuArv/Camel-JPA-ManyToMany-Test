package org.unioulu.jpa.manytomany.example.routes;

import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.unioulu.jpa.manytomany.example.models.Hobby;
import org.unioulu.jpa.manytomany.example.models.Person;

@SuppressWarnings("unchecked")
public class JpaManyToManyExampleRouteBuilder extends RouteBuilder {

    private static final ListJacksonDataFormat PERSON_DATAFORMAT = new ListJacksonDataFormat(Person.class);
    private static final ListJacksonDataFormat HOBBY_DATAFORMAT = new ListJacksonDataFormat(Hobby.class);
    
    @Override
    public void configure() throws Exception {

        onException(Exception.class)
            .handled(true)
            .log(LoggingLevel.ERROR, "${routeId}: Exception: ${exception.message}")
            .log(LoggingLevel.ERROR, "${routeId}: Stacktrace: ${exception.stacktrace}")
        ;

        from("timer:example?repeatCount=1&delay=1000")
            .routeId("exampleTimer")
            .to("direct:jpaManyToManyExampleMainRoute")
        ;

        from("direct:jpaManyToManyExampleMainRoute")
            .routeId("jpaManyToManyExampleMainRoute")
            .log(LoggingLevel.INFO, "Starting jpaManyToManyExampleMainRoute")
            .to("direct:readHobbiesFromFile")
            .to("direct:readPersonsFromFile")
            .split(body())
                .to("direct:mergePersonsToDatabase")
                .to("direct:setHobbiesToPersons")
                .to("direct:mergePersonsToDatabase")
            .end()
            .log(LoggingLevel.INFO, "Finished jpaManyToManyExampleMainRoute")
        ;

        from("direct:readPersonsFromFile")
            .routeId("readPersonsFromFile")
            .pollEnrich("file:{{json.file.path}}?fileName=relationships.json&noop=true", 1000)
            .unmarshal(PERSON_DATAFORMAT)
            .log(LoggingLevel.INFO, "${routeId}: Unmarshalled ${header.CamelFileName} to ${body.class.name}")
        ;

        from("direct:mergePersonsToDatabase")
            .routeId("mergePersonsToDatabase")
            .onException(Exception.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "${routeId}: Exception: ${exception.message}")
                .log(LoggingLevel.ERROR, "${routeId}: Stacktrace: ${exception.stacktrace}")
            .end()
            .to("jpa:" + Person.class.getName())
            .setProperty("person").body()
            .log(LoggingLevel.INFO, "${routeId}: Finished merge")
        ;

        from("direct:readHobbiesFromFile")
            .routeId("readHobbiesFromFile")
            .pollEnrich("file:{{json.file.path}}?fileName=hobbies.json&noop=true", 1000)
            .unmarshal(HOBBY_DATAFORMAT)
            .setProperty("hobbies").body()
            .log(LoggingLevel.INFO, "${routeId}: Unmarshalled ${header.CamelFileName} to ${body.class.name}")
        ;

        from("direct:setHobbiesToPersons")
            .routeId("setHobbiesToPersons")
            .process(this::setHobbiesToPersons)
            .log(LoggingLevel.INFO, "${routeId}: Finished setHobbiesToPersons")
        ;
    }

    private void setHobbiesToPersons(Exchange exchange) {
        final Person PERSON = exchange.getIn().getBody(Person.class);
        final Set<Hobby> HOBBIES = exchange.getProperty("hobbies", Set.class);

        if (PERSON == null) {
            throw new IllegalArgumentException("PERSON is null");
        }

        if (HOBBIES == null) {
            throw new IllegalArgumentException("HOBBIES is null");
        }

        for (Hobby hobby : HOBBIES) {
            PERSON.addHobby(hobby);
            hobby.addPerson(PERSON);
        }

        exchange.getIn().setBody(PERSON);
    }
}
