// script that tests out some of the schema definition capabilities 
// of Titan 0.5.2

def mymethod(g) {
  // try to create objects - but these are duplicates
  // this attempt should start a new transaction
  try {
    v5 = g.addVertexWithLabel('person');
    // should throw an exception
    v5.setProperty('name', 'Matthias');
    g.commit()
    println('successfully inserted a duplicate')
  }
  catch (Exception e) {
    g.rollback();
    println('tried to insert duplicate...handle merge');
  }
}



conf = new BaseConfiguration()
conf.setProperty('storage.backend', 'cassandra')
conf.setProperty('storage.hostname', '9.30.182.235')
conf.setProperty('cache.db-cache', 'true')
conf.setProperty('cache.db-cache-clean-wait', '20')
conf.setProperty('cache.db-cache-time', '180000')
conf.setProperty('cache.db-cache-size', '0.25')

g=TitanFactory.open(conf)

mgmt = g.getManagementSystem()
person = mgmt.makeVertexLabel('person').make();
name = mgmt.makePropertyKey('name').dataType(String.class).cardinality(Cardinality.SINGLE).make()
mgmt.buildIndex('name0idx', Vertex.class).addKey(name).unique().buildCompositeIndex()
emails = mgmt.makeEdgeLabel('emails').multiplicity(Multiplicity.SIMPLE).make()
mgmt.commit()


// create some person verticies with unique names
v1 = g.addVertexWithLabel('person')
v1.setProperty('name', 'Roy')
v2 = g.addVertexWithLabel('person')
v2.setProperty('name', 'Stephen')
v3 = g.addVertexWithLabel('person')
v3.setProperty('name', 'Matthias')
g.commit()

e1 = g.addEdge(null, v1, v2, 'emails')
e2 = g.addEdge(null, v2, v3, 'emails')
e3 = g.addEdge(null, v3, v1, 'emails')
g.commit()

// create new objects - this opens a new  transaction
v4 = g.addVertexWithLabel('person')
v4.setProperty('name', 'David')
e4 = g.addEdge(null, v4, v2, 'emails')
e5 = g.addEdge(null, v4, v1, 'emails')
g.commit();

mymethod(g)
