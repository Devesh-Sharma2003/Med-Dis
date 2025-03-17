package in.kpmg.medicaldisbursement.dtos;

public class ApiResponseStatus {
	
	public static String saved = "Data saved successfully";
	public static String existingCode = "Type code existed already ";
	public static String NoIncometaxSectionFound = "Income tax section not found with id : ";
	public static String NoIncometaxSlabFound = "Income tax slab not found with id : ";
	public static String NoIncometaxDeductionHeadFound = "Income tax deduction head not found with id : ";
	public static String NoIncometaxDeclarationFound = "Income tax declaration not found with id : ";
	public static String NoEmpType = "Employee type not found with id : ";
	public static String NoFyId = "Fy not found with FY id : ";
	public static String existedFY = "Data exist already with given FY ! ";
	public static String existedFYMON = "Data exist already with given Fy and Month! ";
	public static String NoDeductionHeadFound = "Deduction head not found with id : ";
	public static String NoITSectionDeductionHeadFound = "Section-Deduction head mapping not found with id : ";
	public static String existing = "Record existed already ";
	public static String alreadyProcessed = "salary already proccessed for this month for given employee  ";
	public static String fetch = "Data fetched successfully";
	public static String deleted = "Data deleted successfully";
	public static String updated = "Data edited successfully";
	public static String exception = "Unexpected exception";
	public static String validationErrors = "validation errors found";
	public static String badRequest = "Bad Request";
	public static String noRecords = "No records found";
	public static String noEmp = "Employee not found";
	public static String noGroup = "group not found";
	public static String nohoa = "No hoa found";
	public static String noOffice = "No office found";
	public static String groupsExisted = "Group existed already ";
	public static String NotExstedgroup = "Group doesn't exists ";
	public static String alreadyEmpGrp = "Employee mapped to group already ";
	public static String inValidFormula = "Invalid formula !";
	public static String NoProfessionalTaxFound = "Professional Tax not found with id : ";
	public static String noFyMonth = "record existed already with given FY Month ";
	public static String existedHoaFyMonth = "record existed already with given hoa and FY Month ";
}
