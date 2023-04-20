CREATE TABLE IF NOT EXISTS diploma.manager_user (
	id uuid NOT NULL,
	CONSTRAINT manager_user_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS diploma.manager_session (
	id uuid NOT NULL,
	absolute_path varchar(255) NULL,
	description varchar(255) NULL,
	title varchar(255) NULL,
	owner_id uuid NOT NULL,
	s3_folder uuid NOT NULL,
	CONSTRAINT manager_session_pkey PRIMARY KEY (id),
	CONSTRAINT fkkw09ly2w0dp3cew5lvj38rxh7 FOREIGN KEY (owner_id) REFERENCES diploma.manager_user(id)
);