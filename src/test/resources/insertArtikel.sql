insert into artikels(naam, aankoopprijs, verkoopprijs, soort, houdbaarheid,artikelgroepid) values ('testFood', 0.5, 0.9, 'F',4, (select id from artikelgroepen where naam = 'test'));
insert into artikels(naam, aankoopprijs, verkoopprijs, soort, garantie, artikelgroepid) values('testNonFood', 3.1, 4.5, 'NF', 10, (select id from artikelgroepen where naam = 'test'));
insert into kortingen(artikelid, vanafAantal, percentage) values ((select id from artikels where naam ='testFood'), 7, 15);
insert into kortingen(artikelid, vanafAantal, percentage) values ((select id from artikels where naam = 'testNonFood'), 12, 30);
