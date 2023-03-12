drop table Doctors;

create table if not exists Doctors (
    id identity,
    name VARCHAR(50) not null,
    surname VARCHAR(50) not null,
    specialization VARCHAR(25),
    day_On1 integer,
    day_On2 integer
    );
    
drop table Appointments;
create table if not exists Appointments (
    id identity,
    specialization VARCHAR(25),
    type VARCHAR(50),
    price double);

create table if not exists Bookings (
    id identity,
    date DATE
);


create table if not exists Users (
    id identity,
    username VARCHAR(35),
    password VARCHAR(90),
    FIRST_NAME VARCHAR(35),
    surname VARCHAR(35),
    email VARCHAR(35),
    PHONE_NUMBER VARCHAR(35)
);
