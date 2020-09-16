import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'banner',
        loadChildren: () => import('./banner/banner.module').then(m => m.ConvidBannerModule),
      },
      {
        path: 'question',
        loadChildren: () => import('./question/question.module').then(m => m.ConvidQuestionModule),
      },
      {
        path: 'choice',
        loadChildren: () => import('./choice/choice.module').then(m => m.ConvidChoiceModule),
      },
      {
        path: 'answer',
        loadChildren: () => import('./answer/answer.module').then(m => m.ConvidAnswerModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class ConvidEntityModule {}
