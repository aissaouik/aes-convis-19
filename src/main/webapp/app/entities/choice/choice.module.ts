import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ConvidSharedModule } from 'app/shared/shared.module';
import { ChoiceComponent } from './choice.component';
import { ChoiceDetailComponent } from './choice-detail.component';
import { ChoiceUpdateComponent } from './choice-update.component';
import { ChoiceDeleteDialogComponent } from './choice-delete-dialog.component';
import { choiceRoute } from './choice.route';

@NgModule({
  imports: [ConvidSharedModule, RouterModule.forChild(choiceRoute)],
  declarations: [ChoiceComponent, ChoiceDetailComponent, ChoiceUpdateComponent, ChoiceDeleteDialogComponent],
  entryComponents: [ChoiceDeleteDialogComponent],
})
export class ConvidChoiceModule {}
