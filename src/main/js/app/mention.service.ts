import { Injectable } from '@angular/core';
import { Observable, of, Subscription } from 'rxjs';
import { Apollo, gql } from 'apollo-angular';
import { map } from 'rxjs/operators';
import { Relationship } from './relationship';
import { Graph } from './graph';
import { Node } from './node';
import { MentionResponse } from './mentionResponse';
import { ApolloQueryResult } from '@apollo/client/core';

@Injectable({
  providedIn: 'root'
})
export class MentionService {

  constructor(
    private apollo: Apollo
  ) { }

  getMentions(): Observable<Graph> {
    const mentions = this.apollo
      .watchQuery({
        query: gql`
          {
            allMentions {
              source,
              sourceId,
              target,
              targetId,
              type
            }
          }
        `,
      })
      .valueChanges.pipe(map((response: ApolloQueryResult<MentionResponse>) => { return response.data.allMentions }));

    return mentions.pipe(map(this.toGraph));
  }

  toGraph(mentions: Relationship[]): Graph {
    const mentioned = mentions.map((mention: Relationship) => { return { id: mention.targetId, name: mention.target } });
    const mentioners = mentions.map((mention: Relationship) => { return { id: mention.sourceId, name: mention.source } });

    const ids = new Set();
    const removeDuplicates = (data: Node[], item: Node) => {
      let result;

      if(ids.has(item.id)){
        result = data;
      } else {
        ids.add(item.id);
        result = [...data, item];
      }

      return result;
    }
    const nodes = mentioned.concat(mentioners).reduce(removeDuplicates, []);

    const links = mentions.map((mention: Relationship) => { return { source: mention.sourceId, target: mention.targetId, type: mention.type } });

    return { nodes, links }
  }
  

}
