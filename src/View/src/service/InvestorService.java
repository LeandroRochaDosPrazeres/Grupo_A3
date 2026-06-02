package service;

import dao.InvestorDAO;
import dao.LogDAO;
import model.Investor;
import model.LogEntry;
import model.User;
import java.util.List;

public class InvestorService {
    private final InvestorDAO investorDAO;
    private final LogDAO logDAO;

    public InvestorService(InvestorDAO investorDAO, LogDAO logDAO) {
        this.investorDAO = investorDAO;
        this.logDAO = logDAO;
    }

    public Investor createInvestor(Investor investor, User currentUser) {
        if (currentUser == null || !currentUser.isManager()) {
            throw new RuntimeException("Apenas gerentes podem criar investidores");
        }
        investor.setResponsibleManagerId(currentUser.getId());
        Investor saved = investorDAO.create(investor);

        LogEntry log = new LogEntry();
        log.setUserId(currentUser.getId());
        log.setAction("CREATE_INVESTOR");
        log.setDetails(String.format("Gerente '%s' (ID: %d) criou o investidor '%s' (ID: %d) com perfil %s",
                currentUser.getName(), currentUser.getId(),
                saved.getName(), saved.getId(), saved.getRiskProfile()));
        logDAO.create(log);

        return saved;
    }

    public Investor getInvestorById(Long id) {
        return investorDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Investidor não encontrado"));
    }

    // Opcional conforme spec seção 3.2
    public void deleteInvestor(Long investorId, User currentUser) {
        if (currentUser == null || !currentUser.isManager()) {
            throw new RuntimeException("Apenas gerentes podem deletar investidores");
        }
        Investor toDelete = investorDAO.findById(investorId)
                .orElseThrow(() -> new RuntimeException("Investidor não encontrado"));
        investorDAO.delete(investorId);

        LogEntry log = new LogEntry();
        log.setUserId(currentUser.getId());
        log.setAction("DELETE_INVESTOR");
        log.setDetails(String.format("Gerente '%s' (ID: %d) deletou o investidor '%s' (ID: %d)",
                currentUser.getName(), currentUser.getId(),
                toDelete.getName(), toDelete.getId()));
        logDAO.create(log);
    }

    public List<Investor> findByManager(Long managerId) {
        return investorDAO.findByManager(managerId);
    }
}