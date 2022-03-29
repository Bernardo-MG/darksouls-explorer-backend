CREATE (i:Item:Weapon {name: 'Sword'});
CREATE (p:UpgradePath {name: 'Physical'});
CREATE (p:UpgradePath {name: 'Magic'});
CREATE (l:Level {name: 'Sword Physical', target: 'Sword', path: 'Physical', level: 0, physicalDamage: 10, magicDamage: 11, fireDamage: 12, lightningDamage: 13, strengthBonus: 'A', dexterityBonus: 'B', intelligenceBonus: 'C', faithBonus: 'D', physicalReduction: 0.1, magicReduction: 0.2, fireReduction: 0.3, lightningReduction: 0.4});
CREATE (l:Level {name: 'Sword Physical 1', target: 'Sword', path: 'Physical', level: 1, physicalDamage: 20, magicDamage: 21, fireDamage: 22, lightningDamage: 23, strengthBonus: 'A', dexterityBonus: 'B', intelligenceBonus: 'C', faithBonus: 'D', physicalReduction: 0, magicReduction: 0, fireReduction: 0, lightningReduction: 0});
CREATE (l:Level {name: 'Sword Physical 2', target: 'Sword', path: 'Physical', level: 2, physicalDamage: 30, magicDamage: 31, fireDamage: 32, lightningDamage: 33, strengthBonus: 'A', dexterityBonus: 'B', intelligenceBonus: 'C', faithBonus: 'D', physicalReduction: 0, magicReduction: 0, fireReduction: 0, lightningReduction: 0});
CREATE (l:Level {name: 'Sword Physical 3', target: 'Sword', path: 'Physical', level: 3, physicalDamage: 40, magicDamage: 41, fireDamage: 42, lightningDamage: 43, strengthBonus: 'A', dexterityBonus: 'B', intelligenceBonus: 'C', faithBonus: 'D', physicalReduction: 0, magicReduction: 0, fireReduction: 0, lightningReduction: 0});
CREATE (l:Level {name: 'Sword Physical 4', target: 'Sword', path: 'Physical', level: 4, physicalDamage: 50, magicDamage: 51, fireDamage: 52, lightningDamage: 53, strengthBonus: 'A', dexterityBonus: 'B', intelligenceBonus: 'C', faithBonus: 'D', physicalReduction: 0, magicReduction: 0, fireReduction: 0, lightningReduction: 0});
CREATE (l:Level {name: 'Sword Magic', target: 'Sword', path: 'Magic', level: 0, physicalDamage: 10, magicDamage: 11, fireDamage: 12, lightningDamage: 13, strengthBonus: 'A', dexterityBonus: 'B', intelligenceBonus: 'C', faithBonus: 'D', physicalReduction: 0, magicReduction: 0, fireReduction: 0, lightningReduction: 0});
MATCH (l:Level), (m:Level) WHERE l.path = m.path AND m.level = l.level + 1 MERGE (l)-[:NEXT]->(m);
MATCH (l:Level {path: 'Physical', level: 4}), (m:Level {path: 'Magic', level: 0}) MERGE (l)-[:NEXT]->(m);
MATCH (p:UpgradePath {name: 'Physical'}), (l:Level {path: 'Physical'}) MERGE (p)-[:HAS_LEVEL]->(l);
MATCH (p:UpgradePath {name: 'Magic'}), (l:Level {path: 'Magic'}) MERGE (p)-[:HAS_LEVEL]->(l);
MATCH (w:Weapon), (l:Level) WHERE w.name = l.target MERGE (w)-[:HAS_LEVEL]->(l);