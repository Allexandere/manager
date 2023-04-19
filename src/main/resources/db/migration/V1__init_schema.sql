CREATE SCHEMA IF NOT EXISTS diploma;

-- diploma."user" definition

-- Drop table

-- DROP TABLE diploma."user";

CREATE TABLE diploma."user" (
	id uuid NOT NULL,
	CONSTRAINT user_pkey PRIMARY KEY (id)
);


-- diploma."session" definition

-- Drop table

-- DROP TABLE diploma."session";

CREATE TABLE diploma."session" (
	id uuid NOT NULL,
	absolute_path varchar(255) NULL,
	description varchar(255) NULL,
	title varchar(255) NULL,
	owner_id uuid NOT NULL,
	CONSTRAINT session_pkey PRIMARY KEY (id),
	CONSTRAINT fkq9r1nhhkob6tpoq1cbr3yf866 FOREIGN KEY (owner_id) REFERENCES diploma."user"(id)
);