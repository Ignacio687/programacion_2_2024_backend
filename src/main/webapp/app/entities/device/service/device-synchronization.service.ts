import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class DeviceSynchronizationService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api');

  startDeviceSync(syncTimeLaps: number): Observable<null> {
    return this.http.post<null>(`${this.resourceUrl}/startDeviceSync`, null, {
      params: { syncTimeLaps: syncTimeLaps.toString() },
    });
  }

  stopDeviceSync(): Observable<null> {
    return this.http.post<null>(`${this.resourceUrl}/stopDeviceSync`, null);
  }
}
