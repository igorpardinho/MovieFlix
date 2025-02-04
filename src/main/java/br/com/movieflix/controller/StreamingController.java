package br.com.movieflix.controller;

import br.com.movieflix.mapper.StreamingMapper;
import br.com.movieflix.controller.request.StreamingRequest;
import br.com.movieflix.controller.response.StreamingResponse;
import br.com.movieflix.service.StreamingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movieflix/streaming")
public class StreamingController {

    private final StreamingService streamingService;

    public StreamingController(StreamingService streamingService) {
        this.streamingService = streamingService;
    }

    @GetMapping
    public ResponseEntity<List<StreamingResponse>> getAllStreamings() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(streamingService
                        .findAll()
                        .stream()
                        .map(StreamingMapper::toStreamingResponse)
                        .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StreamingResponse> getStreamingById(@PathVariable Long id) {
        return streamingService
                .findById(id)
                .map(streaming -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(StreamingMapper.toStreamingResponse(streaming)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<StreamingResponse> save(@Valid @RequestBody StreamingRequest streamingRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(StreamingMapper
                        .toStreamingResponse(streamingService.save(StreamingMapper
                                .toStreaming(streamingRequest))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        streamingService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
