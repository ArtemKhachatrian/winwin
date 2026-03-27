CREATE TABLE public.users
(
    id            uuid,
    email         varchar(255) NOT NULL,
    password_hash varchar(255) NOT NULL,

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email)
);