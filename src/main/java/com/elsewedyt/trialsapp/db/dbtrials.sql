IF DB_ID('dbtrials') IS NULL EXECUTE('CREATE DATABASE [dbtrials];');
GO

USE [dbtrials];
GO

IF SCHEMA_ID('dbo') IS NULL EXECUTE('CREATE SCHEMA [dbo];');
GO

CREATE  TABLE dbtrials.dbo.countries (
                                         country_id           int    IDENTITY ( 1 , 1 )  NOT NULL,
                                         country_name         nvarchar(50)      NOT NULL,
                                         CONSTRAINT pk_countries PRIMARY KEY  ( country_id )
);
GO

CREATE  TABLE dbtrials.dbo.departments (
                                           department_id        int    IDENTITY  NOT NULL,
                                           department_name      nvarchar(100)      NOT NULL,
                                           CONSTRAINT pk_departments PRIMARY KEY CLUSTERED ( department_id  asc )
);
GO

CREATE  TABLE dbtrials.dbo.file_type (
                                         file_type_id         int    IDENTITY ( 1 , 1 )  NOT NULL,
                                         file_type_name       nvarchar(150)      NULL,
                                         department_id        int      NULL,
                                         CONSTRAINT pk_file_type PRIMARY KEY  ( file_type_id )
);
GO

CREATE  TABLE dbtrials.dbo.matrials (
                                        matrial_id           int    IDENTITY  NOT NULL,
                                        matrial_name         nvarchar(120)      NOT NULL,
                                        CONSTRAINT pk_matrials PRIMARY KEY CLUSTERED ( matrial_id  asc )
);
GO

CREATE  TABLE dbtrials.dbo.sections (
                                        section_id           int    IDENTITY  NOT NULL,
                                        section_name         varchar(100)      NOT NULL,
                                        CONSTRAINT pk_sections PRIMARY KEY CLUSTERED ( section_id  asc )
);
GO

CREATE  TABLE dbtrials.dbo.suppliers (
                                         supplier_id          int    IDENTITY  NOT NULL,
                                         supplier_name        varchar(120)      NOT NULL,
                                         CONSTRAINT pk_suppliers PRIMARY KEY CLUSTERED ( supplier_id  asc )
);
GO

CREATE  TABLE dbtrials.dbo.users (
                                     user_id              int    IDENTITY  NOT NULL,
                                     emp_code             int      NOT NULL,
                                     user_name            varchar(100)      NOT NULL,
                                     password             varchar(50)      NOT NULL,
                                     full_name            nvarchar(100)      NULL,
                                     phone                varchar(11)      NULL,
                                     role                 int      NOT NULL,
                                     active               int      NOT NULL,
                                     creation_date        varchar(50)      NULL,
                                     department_id        int      NOT NULL,
                                     CONSTRAINT pk_users PRIMARY KEY CLUSTERED ( user_id  asc ) ,
                                     CONSTRAINT unq_users_user_name UNIQUE ( user_name )
);
GO

CREATE  TABLE dbtrials.dbo.supplier_country (
                                                sup_country_id       int    IDENTITY  NOT NULL,
                                                supplier_id          int      NULL,
                                                country_id           int      NULL
);
GO

CREATE  TABLE dbtrials.dbo.trials (
                                      trial_id             int    IDENTITY  NOT NULL,
                                      trial_purpose        nvarchar(150)      NULL,
                                      section_id           int      NULL,
                                      matrial_id           int      NULL,
                                      supplier_id          int      NULL,
                                      sup_country_id       int      NULL,
                                      user_id              int      NULL,
                                      creation_date        date      NULL,
                                      notes                nvarchar(200)      NULL,
                                      CONSTRAINT pk_trials PRIMARY KEY CLUSTERED ( trial_id  asc )
);
GO

CREATE  TABLE dbtrials.dbo.files (
                                     file_id              int    IDENTITY  NOT NULL,
                                     creation_date        date      NULL,
                                     file_path            varchar(500)      NULL,
                                     test_situation       int      NULL,
                                     trial_id             int      NULL,
                                     department_id        int      NULL,
                                     user_id              int      NULL,
                                     comment              nvarchar(255)      NULL,
                                     file_type_id         int      NULL,
                                     CONSTRAINT pk_fiels PRIMARY KEY CLUSTERED ( file_id  asc )
);
GO

ALTER TABLE dbtrials.dbo.file_type ADD CONSTRAINT fk_file_type_departments FOREIGN KEY ( department_id ) REFERENCES dbtrials.dbo.departments( department_id );
GO

ALTER TABLE dbtrials.dbo.files ADD CONSTRAINT fk_files_trials FOREIGN KEY ( trial_id ) REFERENCES dbtrials.dbo.trials( trial_id );
GO

ALTER TABLE dbtrials.dbo.files ADD CONSTRAINT fk_files_departments FOREIGN KEY ( department_id ) REFERENCES dbtrials.dbo.departments( department_id );
GO

ALTER TABLE dbtrials.dbo.files ADD CONSTRAINT fk_files_users FOREIGN KEY ( user_id ) REFERENCES dbtrials.dbo.users( user_id );
GO

ALTER TABLE dbtrials.dbo.files ADD CONSTRAINT fk_files_file_type FOREIGN KEY ( file_type_id ) REFERENCES dbtrials.dbo.file_type( file_type_id );
GO

ALTER TABLE dbtrials.dbo.supplier_country ADD CONSTRAINT fk_supplier_country_suppliers FOREIGN KEY ( supplier_id ) REFERENCES dbtrials.dbo.suppliers( supplier_id );
GO

ALTER TABLE dbtrials.dbo.supplier_country ADD CONSTRAINT fk_supplier_country_countries FOREIGN KEY ( country_id ) REFERENCES dbtrials.dbo.countries( country_id );
GO

ALTER TABLE dbtrials.dbo.trials ADD CONSTRAINT fk_trials_matrials FOREIGN KEY ( matrial_id ) REFERENCES dbtrials.dbo.matrials( matrial_id );
GO

ALTER TABLE dbtrials.dbo.trials ADD CONSTRAINT fk_trials_sections FOREIGN KEY ( section_id ) REFERENCES dbtrials.dbo.sections( section_id );
GO

ALTER TABLE dbtrials.dbo.trials ADD CONSTRAINT fk_trials_supplier_country FOREIGN KEY ( sup_country_id ) REFERENCES dbtrials.dbo.supplier_country( sup_country_id );
GO

ALTER TABLE dbtrials.dbo.trials ADD CONSTRAINT fk_trials_suppliers FOREIGN KEY ( supplier_id ) REFERENCES dbtrials.dbo.suppliers( supplier_id );
GO

ALTER TABLE dbtrials.dbo.trials ADD CONSTRAINT fk_trials_users FOREIGN KEY ( user_id ) REFERENCES dbtrials.dbo.users( user_id );
GO

ALTER TABLE dbtrials.dbo.users ADD CONSTRAINT fk_users_departments FOREIGN KEY ( department_id ) REFERENCES dbtrials.dbo.departments( department_id );
GO
