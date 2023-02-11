# Salesman

A salesman is selling a set of products. Each product is described by a set of attributes,
such as name, type, color, cost and weight, each of which may have a different data
type (String, Boolean, Number).

A company is looking to buy products at the best possible prices, and which best match
it’s needs. It has many different products it is looking to purchase. It defines the
products it wishes to buy with a set of rules.

The company realizes that it is very time consuming and error prone to sort through the
salesman’s goods and is looking to implement a system that will:

1. Score all of the salesman’s products on how well they match their product
   definitions by calculating the sum of the rule scores, which is the percentage of
   conditions which match, multiplied by the score.
2. Filter the potential products to just those that pass a given threshold.
3. Calculate the total and average prices for all the products that score sufficiently
   highly.

## Assumptions

* String values are case-insensitive
* Number values are consistent (aka: no mix & matching currencies or imperial/metric values and all same denominations)
* Product attributes are defined up front and are static
* Only one salesman is available for the company to purchase from
  * If allowed more than one salesman, would have another table for the salesman info and an additional property on
    the Product table including the salesman identifier
* There are no Security/Sensitive Data concerns (all data/communication is trusted)
* Audit/Versioning of products' changes are not required
* Rule scores are generated during ingest of Products (or recalculated on Rule additions/changes)
* Certain environment performance considerations are ignored to simplify the POC (aka in-memory db and pre-generated
  test content)

## Diagram (UML)

```mermaid
---
title: Company
---
classDiagram
    class Product
        Product : +Integer id
        Product : +String product_ext_id
        Product : +String name
        Product : +String color
        Product : +Number cost
        Product : +Number quantity
        Product : +Boolean tax_exempt
        Product : +Number score
        Product : +create(product)
        Product : +read(id)
        Product : +update(id, product)
        Product : +delete(id)
        Product : +list(filters)
        Product : +purchases(threshold=50%)

    class Rule
        Rule : +Integer id
        Rule : +String rule
        Rule : +Number score
        Rule : +create(rule)
        Rule : +read(id)
        Rule : +update(id, rule)
        Rule : +delete(id)
        Rule : +list(filters)

    class Condition
        Condition : +Integer rule_id
        Condition : +String name
        Condition : +Operator operator
        Condition : +String value
    
    class ProductRuleScore
        ProductRuleScore : product_id
        ProductRuleScore : rule_id
        ProductRuleScore : score

    Condition "1..n" <..> "1" Rule
    ProductRuleScore "1..n" <..> "1" Rule
    ProductRuleScore "1..n" <..> "1" Product
    
    class Operator
        <<enumeration>> Operator
        Operator : ==
        Operator : <
        Operator : >
        Operator : <=
        Operator : >=
        Operator : !=
```

## Flows

```mermaid
---
title: Ingestion (Push) Flow
---
sequenceDiagram
    Salesman->>Ingestion Service: Product addition/change
    activate Ingestion Service
    Ingestion Service->>Product Queue: Publish to queue for add/update
    Product Queue-->>Ingestion Service: Acknowledge
    Ingestion Service-->>Salesman: Acknowledge
    deactivate Ingestion Service
```

```mermaid
---
title: Ingestion (Pull) Flow
---
sequenceDiagram
    participant Salesman Service
    Ingestion Service->>Salesman Service: Check for Product changes
    activate Ingestion Service
    Salesman Service-->>Ingestion Service: List of Product Changes
    Ingestion Service->>Product Queue: Publish to queue for add/update
    Product Queue-->>Ingestion Service: Acknowledge
    deactivate Ingestion Service
```

```mermaid
---
title: Ingestion Process Flow
---
sequenceDiagram
    Product Queue->>Ingestion Service: Consume Product Change
    activate Ingestion Service
    Ingestion Service->>DB: Process product and update product rule scores
    DB-->>Ingestion Service: Acknowledge
    Ingestion Service-->>Product Queue: Acknowledge
    deactivate Ingestion Service
```

# Score Calculations

During ingestion a score is created per rule and stored. This is done assuming rules get added, removed and updated
during the life of the salesman.

When calculating overall score for a given product it will sum up the individual rule scores and be stored on Product
table (OR on the fly?).

When Company requests the products for purchase (based on score threshold) the backend service will rely on the sql
query (Postgresql) built-in mathematical functions to calculate the totals and average prices of the qualified products.

## Performance Considerations

* If expecting high load for data ingestion, will utilize queues (see ingestion flows above) to minimize wait times.
  **This was not implemented in the deliverable POC.**
* If on the fly calculation of totals and average prices are not performant enough, can pre-calculate the results
  based on various thresholds using CTE/Windows from within the database (postgresql) itself.
