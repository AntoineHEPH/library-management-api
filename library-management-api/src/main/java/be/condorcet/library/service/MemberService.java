package be.condorcet.library.service;

import be.condorcet.library.model.Member;
import be.condorcet.library.repository.MemberRepository;
import be.condorcet.library.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service pour gérer les membres de la bibliothèque.
 * Contient la logique métier liée aux membres.
 */
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Récupère tous les membres.
     */
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    /**
     * Récupère un membre par son ID.
     */
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membre avec l'ID " + id + " non trouvé"));
    }

    /**
     * Crée un nouveau membre.
     */
    public Member createMember(Member member) {
        // Vérifier que l'email n'existe pas déjà
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new RuntimeException("Un membre avec l'email '" + member.getEmail() + "' existe déjà");
        }
        return memberRepository.save(member);
    }

    /**
     * Met à jour un membre existant.
     */
    public Member updateMember(Long id, Member memberDetails) {
        Member member = getMemberById(id);
        
        if (memberDetails.getEmail() != null) {
            // Vérifier que le nouvel email n'existe pas déjà (sauf si c'est le même)
            if (!memberDetails.getEmail().equals(member.getEmail()) && 
                memberRepository.existsByEmail(memberDetails.getEmail())) {
                throw new RuntimeException("Un membre avec l'email '" + memberDetails.getEmail() + "' existe déjà");
            }
            member.setEmail(memberDetails.getEmail());
        }
        if (memberDetails.getFirstName() != null) {
            member.setFirstName(memberDetails.getFirstName());
        }
        if (memberDetails.getLastName() != null) {
            member.setLastName(memberDetails.getLastName());
        }
        if (memberDetails.getMembershipDate() != null) {
            member.setMembershipDate(memberDetails.getMembershipDate());
        }
        if (memberDetails.getActive() != null) {
            member.setActive(memberDetails.getActive());
        }
        
        return memberRepository.save(member);
    }

    /**
     * Supprime un membre par son ID.
     */
    public void deleteMember(Long id) {
        Member member = getMemberById(id);
        memberRepository.delete(member);
    }

    /**
     * Recherche un membre par son email.
     */
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Membre avec l'email '" + email + "' non trouvé"));
    }

    /**
     * Récupère tous les membres actifs.
     */
    public List<Member> getActiveMembers() {
        return memberRepository.findByActiveTrue();
    }

    /**
     * Désactive un membre (suspension du compte).
     */
    public Member suspendMember(Long id) {
        Member member = getMemberById(id);
        member.setActive(false);
        return memberRepository.save(member);
    }

    /**
     * Réactive un membre (levée de suspension).
     */
    public Member activateMember(Long id) {
        Member member = getMemberById(id);
        member.setActive(true);
        return memberRepository.save(member);
    }

    /**
     * Compte le nombre de membres actifs.
     */
    public long countActiveMembers() {
        return memberRepository.countByActiveTrue();
    }

    /**
     * Vérifie si un membre existe avec cet email.
     */
    public boolean memberExists(String email) {
        return memberRepository.existsByEmail(email);
    }
}
