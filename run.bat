@del error.log
@mvn gatling:test -Dgatling.simulationClass=testone.Testing -Ddatafile="data.csv" -Dusername="admin" -Dpassword="Changeme@1"