# Normalization Proofs:

## 1-NF Proof:
- All columns use scalar SQL datatypes like BIGINT, TEXT, DATE, CHAR, INT etc. No Arrays, Set and etc Exists at the column.
- So, each attribute stores only atomic values. So, all relations are in 1-NF.

---

## 2-NF Proof:
- All relations are already in 1-NF. Tables having single-attribute primary keys automatically satisfy 2-NF since no partial dependency is possible.
- For tables with composite primary keys, all non-prime attributes depend on the entire composite key, not on any single attribute of the key.
- Tables with alternate composite candidate keys also have no partial dependency.
- So, no partial dependency exists in any relation. So, all relations are in 2-NF.

---

## 3-NF Proof:
- All relations are already in 2-NF. No non-prime attribute depends on another non-prime attribute (no transitive dependency). Foreign keys only act as references and do not determine other attributes in the same table.
- So, all attributes depend directly on the primary key. So, all relations are in 3-NF.

---

## BCNF Proof:
- For every non-trivial functional dependency X → Y, X is a super key. Tables with a single candidate key automatically satisfy BCNF. Tables with multiple candidate keys have all determinants as candidate keys, hence super keys.
- So, no functional dependency exists where a non-super key determines another attribute.
- So, Most relations in the schema satisfy BCNF because every non-trivial functional dependency has a determinant that is either a primary key or a candidate key. However, a few relations intentionally remain in 3-NF instead of BCNF due to practical design considerations and semantic dependencies created by foreign-key relationships.
- Examples include repositories, branches, repo_tags, repo_releases, and discussion_comments, where certain non-key attributes can functionally determine other attributes through business rules (such as a branch or commit uniquely belonging to one repository). Decomposing these tables further into strict BCNF would increase schema complexity and query overhead without significant practical benefit.
- Therefore, the schema is primarily designed to satisfy 3-NF, while accepting limited BCNF violations for better usability and performance.

---
