--Db-steg för SumNivåer:

--Skapa SumNivå2:
insert into prodn2(kortnamn, id, raderad, prodn1) select distinct(p2.kortnamn), nextval('hibernate_sequence'), false, 1094892562 from prodn2 p2 join prodn1 p1 on p2.prodn1=p1.id where p1.kortnamn like 'HSN 2%' group by p2.kortnamn;

--Skapa SumNivå3:
insert into prodn3 (kortnamn, prodn2, raderad, id)
    (
        select p3.kortnamn as p3kortnamn, (select p2i.id as p2iid from prodn2 p2i where p2i.kortnamn=p2.kortnamn and p2i.prodn1=1094892562), false, nextval('hibernate_sequence')
        from prodn3 p3 join prodn2 p2 on p3.prodn2=p2.id join prodn1 p1 on p2.prodn1=p1.id where p3.prodn2 in (select p2.id from prodn2 p2 join prodn1 p1 on p2.prodn1=p1.id where p1.kortnamn like 'HSN 2%')
        group by (p3kortnamn, p2iid)
    )
