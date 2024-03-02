import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FileSystemFileEntry } from 'ngx-file-drop';
import { Observable } from 'rxjs';
import {UploadVideoResponse} from "./UploadVideoResponse";

@Injectable({
  providedIn: 'root'
})
export class VideoService {

  constructor(private httpClient: HttpClient) { }

  uploadVideo(file: File) : Observable<UploadVideoResponse> {
  const formData = new FormData();
  formData.append('file', file, file.name);
  return this.httpClient.post<UploadVideoResponse>("http://localhost:8080/api/videos/", formData);
  }
}
