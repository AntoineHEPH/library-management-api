package be.condorcet.library.controller;

import be.condorcet.library.model.Member;
import be.condorcet.library.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour gérer les membres de la bibliothèque.
 * Endpoints pour CRUD et gestion des comptes membres.
 */
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * GET /api/members - Récupère tous les membres
     */
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    /**
     * GET /api/members/{id} - Récupère un membre par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Member member = memberService.getMemberById(id);
        return ResponseEntity.ok(member);
    }

    /**
     * POST /api/members - Crée un nouveau membre
     */
    @PostMapping
    public ResponseEntity<Member> createMember(@Valid @RequestBody Member member) {
        Member createdMember = memberService.createMember(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMember);
    }

    /**
     * PUT /api/members/{id} - Met à jour un membre existant
     */
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @Valid @RequestBody Member memberDetails) {
        Member updatedMember = memberService.updateMember(id, memberDetails);
        return ResponseEntity.ok(updatedMember);
    }

    /**
     * DELETE /api/members/{id} - Supprime un membre
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/members/search/email?email=jean@example.com - Recherche par email
     */
    @GetMapping("/search/email")
    public ResponseEntity<Member> searchByEmail(@RequestParam String email) {
        Member member = memberService.getMemberByEmail(email);
        return ResponseEntity.ok(member);
    }

    /**
     * GET /api/members/active - Récupère tous les membres actifs
     */
    @GetMapping("/status/active")
    public ResponseEntity<List<Member>> getActiveMembers() {
        List<Member> activeMembers = memberService.getActiveMembers();
        return ResponseEntity.ok(activeMembers);
    }

    /**
     * POST /api/members/{id}/suspend - Suspend un compte membre
     */
    @PostMapping("/{id}/suspend")
    public ResponseEntity<Member> suspendMember(@PathVariable Long id) {
        Member suspendedMember = memberService.suspendMember(id);
        return ResponseEntity.ok(suspendedMember);
    }

    /**
     * POST /api/members/{id}/activate - Réactive un compte membre
     */
    @PostMapping("/{id}/activate")
    public ResponseEntity<Member> activateMember(@PathVariable Long id) {
        Member activatedMember = memberService.activateMember(id);
        return ResponseEntity.ok(activatedMember);
    }

    /**
     * GET /api/members/stats/active-count - Compte le nombre de membres actifs
     */
    @GetMapping("/stats/active-count")
    public ResponseEntity<Long> countActiveMembers() {
        long count = memberService.countActiveMembers();
        return ResponseEntity.ok(count);
    }
}
