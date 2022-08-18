@del error.log
@mvn gatling:test -Dgatling.simulationClass=testone.Testing -Ddatafile="test.csv" -Dusername="admin" -Dpassword="Changeme@1"