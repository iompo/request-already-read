-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
insert into appuser (id, username, password) values(nextval('hibernate_sequence'), 'tom', 'secret');
insert into appuser (id, username, password) values(nextval('hibernate_sequence'), 'rob', 'secret');
