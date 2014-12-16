conf = new BaseConfiguration()
conf.setProperty('storage.backend', 'cassandra')
conf.setProperty('storage.hostname', 'X.XX.XXX.XXX')
conf.setProperty('cache.db-cache', 'true')
conf.setProperty('cache.db-cache-clean-wait', '20')
conf.setProperty('cache.db-cache-time', '180000')
conf.setProperty('cache.db-cache-size', '0.25')

g=TitanFactory.open(conf)

out = new FileOutputStream('graphout.dat')

GraphSONWriter.outputGraph(g, out)

g.commit()

out.close()
