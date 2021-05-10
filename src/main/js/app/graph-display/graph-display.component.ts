import { Component, OnInit, OnChanges, Input, ViewEncapsulation } from '@angular/core';
import { Graph } from 'app/graph';
import * as d3 from 'd3';

@Component({
  selector: 'app-graph-display',
  encapsulation: ViewEncapsulation.None,
  templateUrl: './graph-display.component.html',
  styleUrls: ['./graph-display.component.sass']
})
export class GraphDisplayComponent implements OnInit, OnChanges {

  @Input() graph: Graph;

  constructor() { }

  ngOnInit(): void {
    this.cleanGraph();
    if (this.graph) {
      this.displayGraph(this.graph);
    }
  }

  ngOnChanges(): void {
    this.cleanGraph();
    if (this.graph) {
      this.displayGraph(this.graph);
    }
  }

  private cleanGraph() {
    d3.select("figure#graph_view").select("svg").remove();
  }

  private displayGraph(data: Graph) {
    const links: any[] = data.links;
    const nodes = data.nodes;

    var width = 800;
    var height = 600;

    const simulation = d3.forceSimulation(nodes)
      .force("link", d3.forceLink(links).id((d: any) => d.id))
      .force("charge", d3.forceManyBody())
      .force("center", d3.forceCenter(width / 2, height / 2));

    var mainView = d3.select("figure#graph_view")
      .append("svg")
      .attr("id", "graph")
      .attr("viewBox", "0 0 " + width + " " + height + "");

    const link = mainView.append("g")
      .selectAll("line")
      .data(links)
      .join("line")
      .attr("class", "graph_link");

    const node = mainView.append("g")
      .selectAll("circle")
      .data(nodes)
      .join("circle")
      .attr("class", "graph_node")
      .call(this.drag);

    node.append("title")
      .text(d => d.name as string);

    simulation.on("tick", () => {
      link
        .attr("x1", d => d.source.x)
        .attr("y1", d => d.source.y)
        .attr("x2", d => d.target.x)
        .attr("y2", d => d.target.y);

      node
        .attr("cx", d => d.x)
        .attr("cy", d => d.y);
    });
  }

  private drag() {
    function dragstarted(event, d) {
      d3.select(this).raise().attr("stroke", "black");
    }

    function dragged(event, d) {
      d3.select(this).attr("cx", d.x = event.x).attr("cy", d.y = event.y);
    }

    function dragended(event, d) {
      d3.select(this).attr("stroke", null);
    }

    return d3.drag()
      .on("start", dragstarted)
      .on("drag", dragged)
      .on("end", dragended);
  }

}