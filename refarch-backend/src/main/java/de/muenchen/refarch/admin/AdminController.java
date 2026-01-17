package de.muenchen.refarch.admin;

import de.muenchen.refarch.admin.dto.AdminStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for CMS admin functionality.
 * All endpoints require the 'writer' role.
 */
@RestController
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    /**
     * Health check endpoint for admin dashboard.
     * Verifies that the user has writer role and admin access is working.
     *
     * @return JSON response with granted status
     */
    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('writer')")
    public AdminStatusResponse getAdminStatus() {
        log.debug("Admin status endpoint accessed");
        return new AdminStatusResponse(true);
    }
}
