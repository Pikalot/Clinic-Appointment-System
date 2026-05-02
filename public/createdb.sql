-- Create the database
CREATE DATABASE Appointments;
USE Appointments;

CREATE TABLE Emergency_Contacts (
    Contact_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    First_name VARCHAR(50) NOT NULL,
    Middle_name VARCHAR(50),
    Last_name VARCHAR(50) NOT NULL,
    Phone VARCHAR(15),
    Version INT NOT NULL DEFAULT 1
);


CREATE TABLE Patients (
    MRN BIGINT PRIMARY KEY AUTO_INCREMENT,
    First_name VARCHAR(50) NOT NULL,
    Middle_name VARCHAR(50),
    Last_name VARCHAR(50) NOT NULL,
    Sex ENUM('Male', 'Female'),
    DoB DATE NOT NULL,
    Password_Hash VARCHAR(255) NOT NULL,
    Contact_ID BIGINT,
    Relationship ENUM('Parents', 'Spouses', 'Children', 'Domestic Partner', 'Other'),
    Version INT NOT NULL DEFAULT 1,
    FOREIGN KEY (Contact_ID) REFERENCES Emergency_Contacts(Contact_ID)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

CREATE TABLE Patient_Addresses (
    Address_ID INT PRIMARY KEY AUTO_INCREMENT,
    Street VARCHAR(255) NOT NULL,
    City VARCHAR(100) NOT NULL,
    State CHAR(2) NOT NULL,
    Zip_code CHAR(5) NOT NULL,
    Type ENUM('Primary', 'Alternate') NOT NULL,
    MRN BIGINT NOT NULL,
    Version INT NOT NULL DEFAULT 1,
    FOREIGN KEY (MRN) REFERENCES Patients(MRN)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE Patient_Phones (
    Phone_ID INT PRIMARY KEY AUTO_INCREMENT,
    Phone VARCHAR(15) NOT NULL,
    Contact_type ENUM('Primary', 'Secondary') NOT NULL,
    MRN BIGINT NOT NULL,
    Version INT NOT NULL DEFAULT 1,
    FOREIGN KEY (MRN) REFERENCES Patients(MRN)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE Patient_Emails (
    Email VARCHAR(255),
    MRN BIGINT NOT NULL,
    Type ENUM('Primary', 'Secondary') NOT NULL,
    Version INT NOT NULL DEFAULT 1,
    PRIMARY KEY (Email, MRN),
    FOREIGN KEY (MRN) REFERENCES Patients(MRN)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE Diagnosis_Codes (
    Code INT PRIMARY KEY AUTO_INCREMENT,
    Diagnosis_Name VARCHAR(255) NOT NULL UNIQUE,
    Version INT NOT NULL DEFAULT 1,
    Is_Active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Restrict update Diagnosis_Codes when it is assigned to Diagnoses table
CREATE TABLE Diagnoses (
    Diagnosis_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    Diagnosis_Date DATE NOT NULL,
    Status VARCHAR(50),
    Severity VARCHAR(50),
    Notes varchar(255),
    MRN BIGINT NOT NULL,
    Code INT NOT NULL,
    Version INT NOT NULL DEFAULT 1,
    FOREIGN KEY (MRN) REFERENCES Patients(MRN)
       ON DELETE CASCADE
       ON UPDATE CASCADE,
    FOREIGN KEY (Code) REFERENCES Diagnosis_Codes(Code)
);

CREATE TABLE Insurance_Companies (
    Company_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(100) NOT NULL,
    Phone VARCHAR(15),
    Email VARCHAR(255),
    Version INT NOT NULL DEFAULT 1
);

CREATE TABLE Insurance_Policies (
    Policy_number BIGINT PRIMARY KEY AUTO_INCREMENT,
    Issued_date DATE,
    Plan_type VARCHAR(50),
    Company_ID BIGINT NOT NULL,
    Version INT NOT NULL DEFAULT 1,
    FOREIGN KEY (Company_ID) REFERENCES Insurance_Companies(Company_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE Covered_By_Ins (
    Policy_number BIGINT,
    Company_ID BIGINT,
    MRN BIGINT NOT NULL,
    Coverage_type VARCHAR(100),
    Version INT NOT NULL DEFAULT 1,
    PRIMARY KEY (Policy_number, Company_ID),
    FOREIGN KEY (MRN) REFERENCES Patients(MRN)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE Clinics (
    Clinic_ID INT PRIMARY KEY AUTO_INCREMENT,
    Street VARCHAR(255) NOT NULL,
    City VARCHAR(100) NOT NULL,
    State CHAR(2) NOT NULL,
    Zip_code CHAR(5) NOT NULL,
    Clinic_name VARCHAR(100),
    Version INT NOT NULL DEFAULT 1
);

CREATE TABLE Services (
    Service_ID INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(255) NOT NULL,
    Fee DECIMAL(10, 2),
    Duration INT DEFAULT 1,
    Version INT NOT NULL DEFAULT 1,
    Is_Active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE Users (
    User_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    First_name VARCHAR(50) NOT NULL,
    Middle_name VARCHAR(50),
    Last_name VARCHAR(50) NOT NULL,
    Username VARCHAR(50),
    Password_Hash VARCHAR(255) NOT NULL,
    Email VARCHAR(255) NOT NULL,
    Clinic_ID INT NOT NULL,
    Version INT NOT NULL DEFAULT 1,
    FOREIGN KEY (Clinic_ID) REFERENCES Clinics(Clinic_ID)
       ON UPDATE CASCADE
);

CREATE TABLE User_Phones (
    Phone_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    Phone VARCHAR(15) NOT NULL,
    Contact_time ENUM('Day time', 'Night time', 'All day'),
    User_ID BIGINT NOT NULL,
    Version INT NOT NULL DEFAULT 1,
    FOREIGN KEY (User_ID) REFERENCES Users(User_ID)
         ON DELETE CASCADE
         ON UPDATE CASCADE
);

CREATE TABLE Staffs (
    Staff_ID BIGINT PRIMARY KEY,
    Employment_Status ENUM(
        'Active',
        'Inactive',
        'Retired',
        'Fired',
        'Resigned',       -- employee left voluntarily
        'On Leave',       -- temporarily away
        'Suspended',      -- temporarily removed
        'Probation'       -- new or under review
    ) NOT NULL DEFAULT 'Active',
    Role ENUM('Admin', 'Staff') NOT NULL DEFAULT 'Staff',
    Version INT NOT NULL DEFAULT 1,
    FOREIGN KEY (Staff_ID) REFERENCES Users(User_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE Provider_Types (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Type VARCHAR(100) NOT NULL UNIQUE,
    Title VARCHAR(25),
    Version INT NOT NULL DEFAULT 1
);

CREATE TABLE Providers (
    User_ID BIGINT PRIMARY KEY,
    License_number VARCHAR(25),
    Type_ID INT,
    Version INT NOT NULL DEFAULT 1,
    FOREIGN KEY (Type_ID) REFERENCES Provider_Types(ID)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

CREATE TABLE Availability_Slots (
    Slot_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    Start_time DATETIME NOT NULL,
    End_time DATETIME NOT NULL,
    Provider_ID BIGINT NOT NULL,
    Status ENUM('Taken', 'Available') NOT NULL DEFAULT 'Available',
    Version INT NOT NULL DEFAULT 1,
    FOREIGN KEY (Provider_ID) REFERENCES Providers(User_ID)
        ON UPDATE CASCADE
);

CREATE TABLE Appointments (
    Appt_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    Appt_Status ENUM('Scheduled', 'Completed', 'Cancelled', 'No Show')
        NOT NULL DEFAULT 'Scheduled',
    Slot_ID BIGINT NOT NULL,
    Service_ID INT NOT NULL,
    MRN BIGINT NOT NULL,
    Version INT NOT NULL DEFAULT 1,
    FOREIGN KEY (Service_ID) REFERENCES Services(Service_ID),
    FOREIGN KEY (Slot_ID) REFERENCES Availability_Slots(Slot_ID)
        ON UPDATE CASCADE,
    FOREIGN KEY (MRN) REFERENCES Patients(MRN)
        ON UPDATE CASCADE
);

CREATE TABLE Audit_Logs (
    Log_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    Timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    Action VARCHAR(255) NOT NULL,
    Target_Type VARCHAR(255),
    Target_ID VARCHAR(255),
    Actor_ID BIGINT,
    FOREIGN KEY (Actor_ID) REFERENCES Users(User_ID)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

-- Create mock data
INSERT INTO Patients(MRN, First_name, Middle_name, Last_name, Sex, DoB, Password_Hash) VALUES
    ('1001', 'John', '', 'Doe','Male','2003-04-09','patient'),
    ('1002', 'Jane', '', 'Joe','Female','2009-10-21','patient'),
    ('1003', 'Pikalot', '', 'Ho','Male','2000-01-01','patient'),
    ('1004', 'First', 'Mid', 'Last','Female','1990-01-01','patient');

INSERT INTO Patient_Addresses(Address_ID, Street, City, State, Zip_code, Type, MRN) VALUES
    ('1', '1 Washington', 'San Jose', 'CA','95111','Primary','1001'),
    ('2', '1880 Tully Rd', 'San Jose', 'CA','95122','Alternate','1001'),
    ('3', '121 N. LaSalle Street', 'Chicago', 'IL','60602','Primary','1002'),
    ('4', '1990 Prince George Dr', 'San Jose', 'CA','95116','Primary','1003'),
    ('5', '1816 Tully Rd', 'San Jose', 'CA','95122','Primary','1004');

INSERT INTO Patient_Phones(Phone_ID, Phone, Contact_type, MRN) VALUES
    ('1', '(408) 123-4567', 'Primary', '1001'),
    ('2', '(669) 121-4545', 'Secondary', '1001'),
    ('3', '(408) 455-5544', 'Primary', '1002'),
    ('4', '(408) 432-1234', 'Secondary', '1002'),
    ('5', '(123) 456-789', 'Primary', '1003');

INSERT INTO Patient_Emails(Email, MRN, Type) VALUES
     ('test1@email.com', '1001', 'Primary'),
     ('test2@email.com', '1001', 'Secondary'),
     ('test3@email.com', '1001', 'Secondary'),
     ('test4@email.com', '1002', 'Primary'),
     ('test5@email.com', '1003', 'Primary'),
     ('test6@email.com', '1004', 'Primary');

INSERT INTO Clinics(Clinic_ID, Clinic_name, Street, City, State, Zip_code) VALUES
    ('1', 'SJSU Clinic', '1 Washington Sq','San Jose','CA','95192'),
    ('2', 'Engineering Affirmary Care', '1816 Tully Rd','San Jose','CA','95122');

INSERT INTO Services(Service_ID, Name, Fee, Duration) VALUES
    ('1', 'Routine Checkup', '150','1'),
    ('2', 'Preventive Care', '250','2'),
    ('3', 'Vaccines and Tests', '100','1'),
    ('4', 'Patient Intake', '500','3');

INSERT INTO Users(User_ID, First_name, Middle_name, Last_name, Username, Password_Hash, Email, Clinic_ID) VALUES
    ('2001', 'Tuan-Anh', '','Ho','provider1', 'provider', 'provider1@gmail.com','1'),
    ('2002', 'Sora', '','Ho','provider2', 'provider', 'provider2@gmail.com','2'),
    ('2003', 'Christina', 'Binh','Vo','provider3', 'provider', 'provider3@gmail.com','1'),
    ('2004', 'Leon', '','Redfield','provider4', 'provider', 'provider4@gmail.com','2');

INSERT INTO User_Phones (Phone_ID, Phone, Contact_time, User_ID) VALUES
    ('1', '(408) 123-1234', 'All day','2001'),
    ('2', '(669) 121-8989', 'Day time','2002'),
    ('3', '(408) 455-6767', 'Night time','2002'),
    ('4', '(408) 111-1234', 'Day time','2003'),
    ('5', '(123) 456-4567', 'All day','2004');

INSERT INTO Staffs(staff_id, employment_status, role) VALUES
    ('2003', 'Active', 'Admin');

INSERT INTO Provider_types (ID, Type, Title) VALUES
    ('1', 'Family Medicine Physicians', 'MD'),
    ('2', 'Nurse Practitioner', 'NP'),
    ('3', 'Physician Assistant', 'PA'),
    ('4', 'Registered Nurse', 'RN'),
    ('5', 'Pediatrician', 'MD');

INSERT INTO Providers(User_ID, License_number, Type_ID) VALUES
    ('2001', 'a111', '1'),
    ('2002', 'b123', '5'),
    ('2004', 'z333', '2');

INSERT INTO Availability_Slots(Slot_ID, Start_time, End_time, Provider_ID, Status) VALUES
	('30001', '2026-04-30 09:00:00', '2026-04-30 10:00:00','2001', 'Taken'),
	('30002', '2026-04-03 09:00:00', '2026-04-03 09:00:00','2004', 'Available'),
	('30003', '2026-04-30 13:00:00', '2026-04-30 14:00:00','2001', 'Available'),
	('30004', '2026-04-30 13:00:00', '2026-04-30 14:00:00','2002', 'Available'),
	('30005', '2026-04-03 09:00:00', '2026-04-03 09:00:00','2004', 'Taken'),
	('30006', '2026-04-30 14:00:00', '2026-04-30 15:00:00','2001', 'Taken');

INSERT INTO Appointments(Appt_ID, Appt_Status, Slot_ID, Service_ID, MRN) VALUES
	('101', 'Scheduled', '30001','1', '1001'),
	('102', 'Scheduled', '30005','2', '1002'),
	('103', 'No Show', '30006','3', '1001');
