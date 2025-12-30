package com.etc.trials.dao;

public class AppContext {

    private static final AppContext INSTANCE = new AppContext();

    private AppContext() {

    }
    private static final SupplierDao supplierDao = new SupplierDao();
    private static final SupplierCountryDao supplierCountryDao = new SupplierCountryDao();
    private static final CountryDao countryDao = new CountryDao();
    private static final DepartmentDao departmentDao = new DepartmentDao();
    private static final TrialDao trialDao = new TrialDao();
    private static final TrialsViewDao trialsViewDao = new TrialsViewDao();
    private static final NewTrialsViewDao newTrialsViewDao = new NewTrialsViewDao();
    private static final SectionDao sectionDao = new SectionDao();
    private static final FileDao fileDao = new FileDao();
    private static final FileViewDao fileViewDao = new FileViewDao();
    private static final FileTypeDao fileTypeDao = new FileTypeDao();
    private static final UserDao userDao = new UserDao();
    private static final MaterialDao materialDao = new MaterialDao();

    public static AppContext getInstance() {
        return INSTANCE;
    }

    public SupplierDao getSupplierDao() {
        return supplierDao;
    }
    public SupplierCountryDao getSupplierCountryDao() {
        return supplierCountryDao;
    }
    public CountryDao getCountryDao() {
        return countryDao;
    }
    public DepartmentDao getDepartmentDao() {
        return departmentDao;
    }
    public TrialDao getTrialDao() {
        return trialDao;
    }
    public TrialsViewDao getTrialsViewDao() {
        return trialsViewDao;
    }
    public NewTrialsViewDao getNewTrialsViewDao() {
        return newTrialsViewDao;
    }
    public SectionDao getSectionDao() {
        return sectionDao;
    }
    public FileDao getFileDao() {
        return fileDao;
    }
    public FileViewDao getFileViewDao() {
        return fileViewDao;
    }
    public FileTypeDao getFileTypeDao() {
        return fileTypeDao;
    }
    public UserDao getUserDao() {
        return userDao;
    }
    public MaterialDao getMaterialDao() {
        return materialDao;
    }


}
