module ScoringSystem {
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.web;
    requires spring.context;
    requires spring.core;
    requires spring.beans;

    exports dk.sdu.mmmi.cbse.scoringsystem  to spring.web;
    opens dk.sdu.mmmi.cbse.scoringsystem to javafx.graphics, spring.core, spring.beans, spring.context;
}