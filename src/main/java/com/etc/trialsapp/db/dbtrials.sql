IF DB_ID('dbtrials') IS NULL EXECUTE('CREATE DATABASE [dbtrials];');
GO

USE [dbtrials];
GO

IF SCHEMA_ID('dbo') IS NULL EXECUTE('CREATE SCHEMA [dbo];');
GO

CREATE  TABLE dbtrials.dbo.countries (
                                         country_id           int    IDENTITY  NOT NULL,
                                         country_name         nvarchar(50)      NOT NULL,
                                         CONSTRAINT pk_countries PRIMARY KEY CLUSTERED ( country_id  asc ) ,
                                         CONSTRAINT unq_countries_country_name UNIQUE ( country_name  asc )
);
GO

CREATE  TABLE dbtrials.dbo.departments (
                                           department_id        int    IDENTITY  NOT NULL,
                                           department_name      nvarchar(100)      NOT NULL,
                                           CONSTRAINT pk_departments PRIMARY KEY CLUSTERED ( department_id  asc ) ,
                                           CONSTRAINT unq_departments_department_name UNIQUE ( department_name  asc )
);
GO

CREATE  TABLE dbtrials.dbo.file_type (
                                         file_type_id         int    IDENTITY  NOT NULL,
                                         file_type_name       nvarchar(150)      NULL,
                                         department_id        int      NULL,
                                         CONSTRAINT pk_file_type PRIMARY KEY CLUSTERED ( file_type_id  asc ) ,
                                         CONSTRAINT unq_file_type_file_type_name UNIQUE ( file_type_name  asc )
);
GO

CREATE  TABLE dbtrials.dbo.materials (
                                         material_id          int    IDENTITY  NOT NULL,
                                         material_name        nvarchar(120)      NOT NULL,
                                         CONSTRAINT pk_matrials PRIMARY KEY CLUSTERED ( material_id  asc ) ,
                                         CONSTRAINT unq_matrials_matrial_name UNIQUE ( material_name  asc )
);
GO

CREATE  TABLE dbtrials.dbo.sections (
                                        section_id           int    IDENTITY  NOT NULL,
                                        section_name         varchar(100)      NOT NULL,
                                        CONSTRAINT pk_sections PRIMARY KEY CLUSTERED ( section_id  asc ) ,
                                        CONSTRAINT unq_sections_section_name UNIQUE ( section_name  asc )
);
GO

CREATE  TABLE dbtrials.dbo.suppliers (
                                         supplier_id          int    IDENTITY  NOT NULL,
                                         supplier_name        varchar(120)      NOT NULL,
                                         CONSTRAINT pk_suppliers PRIMARY KEY CLUSTERED ( supplier_id  asc ) ,
                                         CONSTRAINT unq_suppliers_supplier_name UNIQUE ( supplier_name  asc )
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
                                     CONSTRAINT unq_users_user_name UNIQUE ( user_name  asc )
);
GO

CREATE  TABLE dbtrials.dbo.supplier_country (
                                                sup_country_id       int    IDENTITY  NOT NULL,
                                                supplier_id          int      NULL,
                                                country_id           int      NULL,
                                                CONSTRAINT pk_supplier_country PRIMARY KEY CLUSTERED ( sup_country_id  asc )
);
GO

CREATE  TABLE dbtrials.dbo.trials (
                                      trial_id             int    IDENTITY  NOT NULL,
                                      trial_purpose        nvarchar(150)      NOT NULL,
                                      section_id           int      NULL,
                                      material_id          int      NULL,
                                      supplier_id          int      NULL,
                                      user_id              int      NULL,
                                      sup_country_id       int      NULL,
                                      creation_date        datetime2      NULL,
                                      notes                nvarchar(200)      NULL,
                                      CONSTRAINT pk_trials PRIMARY KEY CLUSTERED ( trial_id  asc )
);
GO

CREATE  TABLE dbtrials.dbo.files (
                                     file_id              int    IDENTITY  NOT NULL,
                                     creation_date        datetime2      NULL,
                                     file_path            nvarchar(500)      NULL,
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

ALTER TABLE dbtrials.dbo.files ADD CONSTRAINT fk_files_departments FOREIGN KEY ( department_id ) REFERENCES dbtrials.dbo.departments( department_id );
GO

ALTER TABLE dbtrials.dbo.files ADD CONSTRAINT fk_files_file_type FOREIGN KEY ( file_type_id ) REFERENCES dbtrials.dbo.file_type( file_type_id );
GO

ALTER TABLE dbtrials.dbo.files ADD CONSTRAINT fk_files_trials FOREIGN KEY ( trial_id ) REFERENCES dbtrials.dbo.trials( trial_id );
GO

ALTER TABLE dbtrials.dbo.files ADD CONSTRAINT fk_files_users FOREIGN KEY ( user_id ) REFERENCES dbtrials.dbo.users( user_id );
GO

ALTER TABLE dbtrials.dbo.supplier_country ADD CONSTRAINT fk_supplier_country_countries FOREIGN KEY ( country_id ) REFERENCES dbtrials.dbo.countries( country_id );
GO

ALTER TABLE dbtrials.dbo.supplier_country ADD CONSTRAINT fk_supplier_country_suppliers FOREIGN KEY ( supplier_id ) REFERENCES dbtrials.dbo.suppliers( supplier_id );
GO

ALTER TABLE dbtrials.dbo.trials ADD CONSTRAINT fk_trials_matrials FOREIGN KEY ( material_id ) REFERENCES dbtrials.dbo.materials( material_id );
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

