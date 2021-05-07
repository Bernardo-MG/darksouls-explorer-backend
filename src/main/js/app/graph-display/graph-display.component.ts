import { Component, OnInit } from '@angular/core';
import { Graph } from 'app/graph';
import * as d3 from 'd3';
import { MentionService } from '../mention.service';

@Component({
  selector: 'app-graph-display',
  templateUrl: './graph-display.component.html',
  styleUrls: ['./graph-display.component.sass']
})
export class GraphDisplayComponent implements OnInit {

  constructor(
    private mentionService: MentionService
  ) { }

  ngOnInit(): void {
    this.mentionService.getMentions().subscribe(this.displayGraph);
  }

  private displayGraph(data: Graph) {
    const links : any[] = data.links;
    const nodes = data.nodes;

    const simulation = d3.forceSimulation(nodes)
      .force("link", d3.forceLink(links).id((d: any) => d.id))
      .force("charge", d3.forceManyBody())
      .force("x", d3.forceX())
      .force("y", d3.forceY());

    var mainView = d3.select("figure#graph_view")
      .append("svg")
      .attr("id", "graph")
      .attr("class", "full_size");
    var viewNode = mainView.node();
    var width = (viewNode as SVGGElement).clientWidth;
    var height = (viewNode as SVGGElement).clientHeight;

    const link = mainView.append("g")
      .attr("stroke", "#999")
      .attr("stroke-opacity", 0.6)
      .selectAll("line")
      .data(links)
      .join("line")
      .attr("stroke-width", d => Math.sqrt(2))
      .attr("transform", "translate(" + [(width / 2), (height / 2)] + ")");

    const node = mainView.append("g")
      .attr("stroke", "#fff")
      .attr("stroke-width", 1.5)
      .selectAll("circle")
      .data(nodes)
      .join("circle")
      .attr("r", 5)
      .attr("transform", "translate(" + [(width / 2), (height / 2)] + ")");
      //.call(this.drag(simulation));

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

  private drag(simulation) {
    function dragstarted(event, d) {
      if (!event.active) simulation.alphaTarget(0.3).restart();
      d.fx = d.x;
      d.fy = d.y;
    }

    function dragged(event, d) {
      d.fx = event.x;
      d.fy = event.y;
    }

    function dragended(event, d) {
      if (!event.active) simulation.alphaTarget(0);
      d.fx = null;
      d.fy = null;
    }

    return d3.drag()
      .on("start", dragstarted)
      .on("drag", dragged)
      .on("end", dragended);
  }

}
