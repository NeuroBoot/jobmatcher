# jobmatcher
semantic job matching system
---

## 🚀 Core Components

* **Data Storage:** Centralized management of candidate and job profiles within an RDF triple-store.
* **Analysis Engine:** Employs the OWL API to validate the ontology schema and extract professional entities.
* **Matching Logic:** Uses SPARQL-based inference to identify relationships.
* **Reporting:** Generates HR-friendly dashboards and automated candidate shortlists with quantitative fit scores.

---

## 🧠 The Ontology Framework

The intelligence of the system is defined by its underlying RDF structure, which categorizes data into logical classes and properties.

### 4.1 Key Classes

* **Job / JobSeeker:** The primary entities being matched.
* **Skill:** Standardized competencies (e.g., `ProgrammingSkill`).
* **ExperienceLevel:** Categorization of seniority (e.g., senior, junior).
* **Location:** Geographic entities.

### 4.2 Semantic Relationships (Object Properties)

The system infers "Fit" by analyzing specific RDF properties that link the classes above:

* **hasSkill / requiresSkill:** Direct alignment between candidate capabilities and role requirements.
* **hasExperience / requireExperience:** Mapping individual seniority to the necessary professional level.
* **preferredLocation / locatedIn:** Geographic filtering to match seeker preferences with office locations.
