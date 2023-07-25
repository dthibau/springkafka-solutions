DROP TABLE IF EXISTS movies;
create table movies (
id serial,
director character varying(255) NOT NULL,
title  character varying(255) NOT NULL,
duration integer,
stock integer,
CONSTRAINT movie_pkey PRIMARY KEY (id)
);

insert into movies (director,title,duration,stock) values('Hitchcock','Les trente-neuf marches',86,2);
insert into movies (director,title,duration,stock) values('Huston','Key Largo',101,1);
insert into movies (director,title,duration,stock) values('HITCHCOCK','Fenêtre sur cour',112,5);

