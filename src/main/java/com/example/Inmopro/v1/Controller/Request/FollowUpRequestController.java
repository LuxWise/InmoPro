package com.example.Inmopro.v1.Controller.Request;

import com.example.Inmopro.v1.Model.Request.FollowUpRequest;
import com.example.Inmopro.v1.Service.Request.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/followup-requests")
@RequiredArgsConstructor
public class FollowUpRequestController {

    private final RequestService requestService;

    @GetMapping()
    public ResponseEntity<List<FollowUpRequest>> getAllFollowUpRequests() {
        return ResponseEntity.ok(requestService.getFollowUpRequest());
    }

    @GetMapping("/status/{statusName}")
    public ResponseEntity<List<Object[]>> getFollowUpRequestsByStatusName(@PathVariable String statusName) {
        List<Object[]> followUpRequests = requestService.getFollowUpRequestsByStatusName(statusName);
        if (followUpRequests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(followUpRequests);
    }
}
