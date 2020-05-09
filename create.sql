create database moviedbtest;
use moviedbtest;
create table movies(id varchar(10) primary key,title varchar(100) not null default "", year int not null , director varchar(100) not null default "");
create table stars(id varchar(10) primary key NOT NULL,name varchar(100) not null default '', birthYear int NULL);
create table stars_in_movies(starId varchar(10) NOT NULL,movieId varchar(10) NOT NULL, FOREIGN KEY(starId) references stars(id),FOREIGN KEY(movieId) references movies(id)); 
create table genres(id int UNSIGNED NOT NULL auto_increment primary key,name varchar(32) not null default '');
create table genres_in_movies(genreId int unsigned NOT NULL ,movieId varchar(10) not null default '', 
foreign key(genreId) references genres(id),foreign key(movieId)references movies(id));
create table creditcards ( id varchar(20) NOT NULL primary key,firstName varchar(50) not null default '', lastName varchar(50)
not null default '', expiration date NOT NULL);
create table customers(id int NOT NULL auto_increment primary key,firstName varchar(50) not null default '', 
lastName varchar(50) not null default '', ccId varchar(20), address varchar(200)  not null default '', 
email varchar(50) not null default '', password varchar(20) not null default '', foreign key(ccId) references creditcards (id));
create table sales(id int NOT NULL auto_increment primary key, customerId int references customers(id), 
movieId varchar(10) not null default '' references movies(id), saleDate date NOT NULL);


create table ratings(movieId varchar(10) references movies(id), rating float NOT NULL, numVotes int NULL NULL);

# CREATE INDEX id_index ON movies (id);
# create table employees(email varchar(50) primary key, password varchar(20) not null, fullname varchar(100));
# insert into employees values("classta@email.edu", "classta", "TA CS122B");