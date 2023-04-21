CREATE TABLE diploma.manager_user (
	id uuid NOT NULL,
	CONSTRAINT manager_user_pkey PRIMARY KEY (id)
);

CREATE TABLE diploma.manager_session (
	id uuid NOT NULL,
	absolute_path varchar(255) NULL,
	description varchar(255) NULL,
	title varchar(255) NULL,
	owner_id uuid NOT NULL,
	CONSTRAINT manager_session_pkey PRIMARY KEY (id),
	CONSTRAINT fkkw09ly2w0dp3cew5lvj38rxh7 FOREIGN KEY (owner_id) REFERENCES diploma.manager_user(id)
);

CREATE TABLE diploma.manager_snapshot (
	id uuid NOT NULL,
	creation_date timestamp NULL,
	description varchar(255) NULL,
	title varchar(255) NULL,
	owner_id uuid NOT NULL,
	CONSTRAINT manager_snapshot_pkey PRIMARY KEY (id),
	CONSTRAINT fk8jfn6ykb8eaeg4rvu6kt59jyw FOREIGN KEY (owner_id) REFERENCES diploma.manager_session(id)
);