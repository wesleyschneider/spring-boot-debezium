-- Configura Database
CREATE DATABASE DEBEZIUM_CDC
GO

USE DEBEZIUM_CDC
GO

CREATE SCHEMA target;
GO

CREATE TABLE Estudante (
    cd_estudante integer identity primary key,
    nome varchar(100),
    nome_social varchar(100),
    email varchar(255),
    dt_alteracao_cdc datetime2
);

CREATE TABLE target.usuario (
    cd_usuario integer identity primary key,
    nome varchar(100),
    nome_social varchar(100),
    dt_alteracao datetime2,
    cd_estudante integer
);

CREATE TABLE target.usuario_email(
    cd_email integer identity primary key,
    email varchar(255),
    dt_alteracao datetime2,
    cd_usuario integer,
    FOREIGN KEY (cd_usuario) REFERENCES target.usuario(cd_usuario)
);

EXEC sys.sp_cdc_enable_db;
GO

EXEC sys.sp_cdc_enable_table
@source_schema = N'dbo',
@source_name   = N'Estudante',
@role_name     = NULL,
@supports_net_changes = 1
GO

EXEC sys.sp_cdc_enable_table
@source_schema = N'target',
@source_name   = N'usuario',
@role_name     = NULL,
@supports_net_changes = 1
GO

EXEC sys.sp_cdc_enable_table
@source_schema = N'target',
@source_name   = N'usuario_email',
@role_name     = NULL,
@supports_net_changes = 1
GO

CREATE TRIGGER update_dt_alteracao_cdc_on_change
    ON Estudante
    AFTER UPDATE AS
    IF NOT UPDATE(dt_alteracao_cdc)
BEGIN
        SET NOCOUNT ON

        DECLARE @CdEstudante INT

SELECT @CdEstudante = inserted.cd_estudante
FROM INSERTED

UPDATE Estudante SET dt_alteracao_cdc=GETDATE()
WHERE cd_estudante=@CdEstudante
END;