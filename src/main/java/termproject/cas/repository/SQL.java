package termproject.cas.repository;

public class SQL {
    public static final String FIND_ALL_APPOINTMENTS = """
        SELECT
            A.*,
            SE.Name AS Service_Name,
            SE.Fee,
            SE.Duration,
            S.Start_time,
            S.End_time,
            S.Provider_ID,
            S.Status,
            U.Last_name,
            U.Clinic_ID,
            T.Type,
            T.Title,
            C.Street,
            C.City,
            C.State,
            C.Zip_code,
            C.Clinic_name,
            Pa.First_name AS Patient_FN,
            Pa.Middle_name AS Patient_MN,
            Pa.Last_name AS Patient_LN,
            Pa.Sex AS Patient_Sex,
            Pa.DoB AS Patient_DoB
        FROM Appointments A
        JOIN Services SE ON A.Service_ID = SE.Service_ID AND SE.Is_Active = TRUE
        JOIN Availability_Slots S ON A.Slot_ID = S.Slot_ID
        JOIN Users U ON U.User_ID = S.Provider_ID
        JOIN Providers P ON S.Provider_ID = P.User_ID
        JOIN Provider_Types T ON P.Type_ID = T.ID
        JOIN Clinics C ON C.Clinic_ID = U.Clinic_ID
        JOIN Patients Pa ON Pa.MRN = A.MRN
        """;

    public static final String FIND_APPOINTMENTS_BY_MRN = FIND_ALL_APPOINTMENTS + """
        WHERE A.MRN = ?
        """;

    public static final String FIND_APPOINTMENTS_BY_PROVIDER = FIND_ALL_APPOINTMENTS + """
        WHERE S.Provider_ID = ?
        """;

    public static final String FIND_APPOINTMENT_BY_ID = FIND_ALL_APPOINTMENTS + """
        WHERE A.Appt_ID = ?
        """;

    public static final String FIND_ALL_SLOTS = """
        SELECT
            S.Slot_ID,
            S.Start_time,
            S.End_time,
            S.Provider_ID AS User_ID,
            S.Status,
            S.Version,
            U.First_name,
            U.Middle_name,
            U.Last_name,
            U.Email,
            U.Clinic_ID,
            T.Type,
            T.Title,
            C.Street,
            C.City,
            C.State,
            C.Zip_code,
            C.Clinic_name
        FROM Availability_Slots S
        JOIN Users U ON U.User_ID = S.Provider_ID
        JOIN Providers P ON S.Provider_ID = P.User_ID
        JOIN Provider_Types T ON P.Type_ID = T.ID
        JOIN Clinics C ON C.Clinic_ID = U.Clinic_ID
        """;

    public static final String FIND_SLOT_BY_ID = FIND_ALL_SLOTS + """
        WHERE S.Slot_ID = ?
        """;

    public static final String FIND_SLOT_BY_PROVIDER_ID = FIND_ALL_SLOTS + """
        WHERE S.User_ID = ?
        """;

    public static final String FIND_ALL_PATIENTS = """
        SELECT
            P.MRN,
            P.First_name,
            P.Last_name,
            P.DoB,
            P.Sex,
            P.Version
        FROM Patients P
        """;

    public static final String FIND_PATIENT_BY_MRN = FIND_ALL_PATIENTS + """
        WHERE P.MRN = ?
        """;

    public static final String FIND_ALL_PROVIDERS = """
        SELECT
            P.*,
            U.First_name,
            U.Middle_name,
            U.Last_name,
            U.Email,
            U.Clinic_ID,
            T.Type,
            T.Title
        FROM Providers P
        JOIN Users U ON U.User_ID = P.User_ID 
        JOIN Provider_Types T ON P.Type_ID = T.ID
        """;

    public static final String FIND_PROVIDER_BY_ID = FIND_ALL_PROVIDERS + """
        WHERE P.User_ID = ?
        """;

    public static final String FIND_ALL_CLINICS = """
        SELECT * FROM Clinics
        """;

    public static final String FIND_CLINIC_BY_ID = FIND_ALL_CLINICS + """
        WHERE Clinic_ID = ?
        """;
}