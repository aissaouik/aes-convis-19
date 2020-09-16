import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBanner } from 'app/shared/model/banner.model';
import { BannerService } from './banner.service';

@Component({
  templateUrl: './banner-delete-dialog.component.html',
})
export class BannerDeleteDialogComponent {
  banner?: IBanner;

  constructor(protected bannerService: BannerService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bannerService.delete(id).subscribe(() => {
      this.eventManager.broadcast('bannerListModification');
      this.activeModal.close();
    });
  }
}
