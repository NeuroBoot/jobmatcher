package com.jobmatch;

import org.apache.jena.rdf.model.*;
import org.apache.jena.query.*;
import org.apache.jena.update.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import java.io.File;

public class fusekimatcher {


    private static Model model;

        public static void main(String[] args) throws Exception {
            System.out.println(" Job Matching System");
            System.out.println("=======================");

            loadData();
            analyzeOntology();
            inferRule();
            finalQuery();
        }

        static void loadData() {
            model = ModelFactory.createDefaultModel();
            String rdfPath = "path"; //path
            model.read(rdfPath);
            System.out.println(" RDF loaded: " + model.size() + " triples");
        }

        static void analyzeOntology() throws Exception {
            try {
                OWLOntologyManager mgr = OWLManager.createOWLOntologyManager();
                OWLOntology ont = mgr.loadOntologyFromOntologyDocument(
                        new File("path")
                );

                System.out.println("\n OWL ANALYSIS:");
                System.out.println("  Axioms: " + ont.axioms().count());
                System.out.println("  Classes: " + ont.classesInSignature().count());
            } catch (Exception e) {
                System.out.println("\n owl problem");
            }
        }


        static void inferRule() {
            System.out.println("\n >>> rule is good");

                String rule = """
        PREFIX : <http://exemple.org/jobmatch#>
        INSERT { 
            ?seeker :perfectMatch ?job .
        }
        WHERE {
          
            ?seeker :name ?name .
            ?job :title ?jobTitle .
            
          
            ?seeker :hasSkill ?sharedSkill .
            ?job :requiresSkill ?sharedSkill .

      
            FILTER NOT EXISTS {
                ?job :requiresSkill ?required .
                FILTER NOT EXISTS { ?seeker :hasSkill ?required }
            }
        }
        """;

                UpdateAction.parseExecute(rule, model);
                System.out.println(" >>> rule successfully.");
            }

        static void finalQuery() {

            String query = """
            PREFIX : <http://exemple.org/jobmatch#>
            SELECT ?seeker ?name ?job ?title (COUNT(?skill) AS ?score) ?status
            WHERE {
                ?seeker :hasSkill ?skill .
                ?job :requiresSkill ?skill .
                ?seeker :name ?name .
                ?job :title ?title .
                BIND(IF(EXISTS { ?seeker :perfectMatch ?job }, "Perfect", "Partial") AS ?status)
            }
            GROUP BY ?seeker ?name ?job ?title ?status
            ORDER BY DESC(?status) DESC(?score)
            """;

            printResults(query);
        }

    private static void printResults(String q) {
        System.out.println("\n HR JOB MATCHES");

        System.out.println("══════════════════════════════════════════════════════════════");

        System.out.println("| Name         | Title            | Score | Status  |");
        System.out.println("══════════════════════════════════════════════════════════════");

        try (QueryExecution exec = QueryExecutionFactory.create(q, model)) {
            ResultSet rs = exec.execSelect();
            while (rs.hasNext()) {
                QuerySolution sol = rs.next();
                String name = sol.getLiteral("name").getString();
                String title = sol.getLiteral("title").getString();
                int score = sol.getLiteral("score").getInt();
                String status = sol.getLiteral("status").getString();


                System.out.printf("| %-12s | %-16s | %-5d | %-7s |\n",
                        name, title, score, status);
            }
        } catch (Exception e) {
            System.err.println(" Query Error: " + e.getMessage());
        }

        System.out.println("══════════════════════════════════════════════════════════════\n");
    }
    }
