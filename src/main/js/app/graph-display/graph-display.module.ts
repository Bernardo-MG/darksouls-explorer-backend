import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterTestingModule } from "@angular/router/testing";

import { FlexLayoutModule } from '@angular/flex-layout';

import { MatCardModule } from '@angular/material/card';

import { GraphViewComponent } from './graph-view/graph-view.component';
import { GraphStatusComponent } from './graph-status/graph-status.component';

@NgModule({
  declarations: [
    GraphViewComponent,
    GraphStatusComponent
  ],
  imports: [
    CommonModule,
    RouterTestingModule,
    FlexLayoutModule,
    MatCardModule
  ],
  exports: [
    CommonModule,
    RouterTestingModule,
    FlexLayoutModule,
    MatCardModule,
    GraphViewComponent
  ]
})
export class GraphDisplayModule { }
