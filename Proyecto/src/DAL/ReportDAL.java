package DAL;

import ET.Report;

import java.io.*;
import java.util.ArrayList;

public class ReportDAL {
    private static final String DATA_DIR = "Proyecto/data/Reports";
    private static final String FILE_PATH = DATA_DIR + File.separator + "reports.dat";

    private static ArrayList<Report> listOfReports = new ArrayList<>();
    private static int idCounter = 0;
    public ReportDAL() throws Exception{
        listOfReports = loadReports();
    }
    public void insertReport(Report reportToInsert) throws Exception{
        reportToInsert.setId(idCounter++);
        listOfReports.add(reportToInsert);
        saveReports();
    }
    public ArrayList<Report> getReports(){
        return listOfReports;
    }
    public void updateReport(Report reportToUpdate) throws Exception{
        for (int i = 0; i < listOfReports.size(); i++){
            if(listOfReports.get(i).getId() == reportToUpdate.getId()){
                listOfReports.set(i, reportToUpdate);
                saveReports();
                return;
            }
        }
    }
    public void deleteReport(Report reportToDelete) throws Exception{
        for (int i = 0; i < listOfReports.size(); i++){
            if(listOfReports.get(i).getId() == reportToDelete.getId()){
                listOfReports.remove(i);
                saveReports();
                return;
            }
        }
    }
    private void saveReports() throws Exception{
        try (ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            fos.writeObject(listOfReports);
        }
    }
    @SuppressWarnings("unchecked")
    private ArrayList<Report> loadReports() throws Exception{
        File file = new File(FILE_PATH);
        if (!file.exists()){
            return new ArrayList<>();
        }
        try (ObjectInputStream fis = new ObjectInputStream(new FileInputStream(FILE_PATH))){
            return (ArrayList<Report>) fis.readObject();
        }
    }
}
