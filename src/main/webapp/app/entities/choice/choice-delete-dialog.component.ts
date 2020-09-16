import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IChoice } from 'app/shared/model/choice.model';
import { ChoiceService } from './choice.service';

@Component({
  templateUrl: './choice-delete-dialog.component.html',
})
export class ChoiceDeleteDialogComponent {
  choice?: IChoice;

  constructor(protected choiceService: ChoiceService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.choiceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('choiceListModification');
      this.activeModal.close();
    });
  }
}
